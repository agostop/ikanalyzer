package com.rongke.message.filter;

import com.rongke.analyzer.cfg.Configuration;
import com.rongke.message.filter.config.RongkeIKAnalyzer;
import com.rongke.message.filter.config.RongkeIKConfig;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

public abstract class MessageFilter {
	private static final Logger m_logger = Logger.getLogger(MessageFilter.class);
	private RongkeIKAnalyzer m_analyzer;
	private RAMDirectory  m_directory;
	private IndexSearcher m_isearcher;
	private QueryParser m_qp;

	private Configuration ikCfg;

	public MessageFilter(String configPath) {
		this.ikCfg = new RongkeIKConfig();
		this.ikCfg.setUseSmart(true);
		this.m_analyzer = new RongkeIKAnalyzer(this.ikCfg);
	}


	public void initIdxDir() {
		
		try {
			m_directory = new RAMDirectory();
			
			IndexReader reader = DirectoryReader.open(m_directory);
			m_isearcher = new IndexSearcher(reader);
			
			String fieldName = "text";
			m_qp = new QueryParser(fieldName, m_analyzer);
			m_qp.setDefaultOperator(QueryParser.OR_OPERATOR);
		} catch (IOException e) {
			m_logger.error(e);
		}finally {
		    m_directory.close();
        }
		
	}
	
	public boolean grep(String keyword) {
		try {
			if (m_isearcher == null) {
				initIdxDir();
			}
			
			Query query = m_qp.parse(QueryParser.escape(keyword));

			// 搜索相似度最高的5条记录
			TopScoreDocCollector collector = TopScoreDocCollector.create(5);
			m_isearcher.search(query, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			if (hits.length > 0) {
				return true;
			}
			
			/*
			for (int i = 0; i < hits.length; ++i) {
				int docId = hits[i].doc;
				Document d = isearcher.doc(docId);
				System.out.println(d.getFields());
			}
			*/
			
		} catch (Exception e) {
			m_logger.error(e);
		} 
		/*
		 * finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					m_logger.error(e);
				}
			}
		}
		 */
		
		return false;
		
	}
	
	private ArrayList<String> readExtDict(String extDictPath) {
		ArrayList<String> dictArray = new ArrayList<String>();
		//File dir = new File("./config/dict");
		File dir = new File(extDictPath);
		File[] listFiles = dir.listFiles();
		assert listFiles != null;
		for (File f : listFiles) {
			if (f.isFile()) {
				FileInputStream dict = null;
				try {

					dict = new FileInputStream(f);
					BufferedReader reader = null;
					try {
						reader = new BufferedReader(new InputStreamReader(dict));
						String str = null;
						while ((str = reader.readLine()) != null) {
							dictArray.add(str);
						}

					} catch (Exception e) {
						m_logger.error(e);
						continue;
					} finally {
						reader.close();
					}

				} catch (Exception e) {
					m_logger.error(e);
				} finally {
					try {
						dict.close();
					} catch (IOException e) {
						m_logger.error(e);
					}
				}
			}
		}
		
		return dictArray;
	}
	
	public void crtDoc(String extDir) {

		ArrayList<String> dictArray = readExtDict(extDir);

		IndexWriter iwriter = null;

		String fieldName = "text";

		try {
			Directory directory = new RAMDirectory();
			//directory = NIOFSDirectory.open(Paths.get("E:\\TMP\\indexdir"));
			IndexWriterConfig config = new IndexWriterConfig(m_analyzer);

			iwriter = new IndexWriter(directory, config);
			TextField tField = new TextField(fieldName, "", Field.Store.YES);
			Document doc = null;
			int size = dictArray.size();
			for (int i = 0; i < size ; i++) {
				tField.setStringValue(dictArray.get(i));
				doc = new Document();
				doc.add(tField);
				iwriter.addDocument(doc);
				doc = null;
			}
		} catch (Exception e) {
			m_logger.error(e);
		} finally {
			try {
				iwriter.close();
			} catch (IOException e) {
				m_logger.error(e);
			}
		}

		/*
		 * try { 
		 * 		getDoc(directory); 
		 * } catch (IOException e1) { 
		 * 		catch block e1.printStackTrace(); 
		 * }
		 */
	}

	/*
	public void getDoc(Directory directory) throws IOException {

		// Directory directory = FSDirectory.open(new File(path));
		IndexReader r = DirectoryReader.open(directory);
		IndexSearcher is = new IndexSearcher(r);
		Fields fields = MultiFields.getFields(r);
		System.out.println(fields.size());
		
//		Iterator<String> it = fields.iterator(); while(it.hasNext()){
//		System.out.println(it.next()); }
//		System.out.println(fields.terms("text").getDocCount());
		

		int count = fields.terms("text").getDocCount();
		// System.out.println(count);
		for (int i = 0; i < count; i++) {
			Document doc = is.doc(i);
			System.out.println(doc.getFields());
			System.out.println(doc.getField("text"));
		}
		r.close();

	}
	*/
	
	public static void main(String[] args) {
//		MessageFilter mf = new MessageFilter("your configpath", " your indexdir");
//		mf.grep("江阴毛纺厂");
	}

}
