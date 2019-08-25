package com.rongke.message.filter.config;

import com.rongke.analyzer.cfg.Configuration;
import com.rongke.analyzer.lucene.IKTokenizer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

public final class RongkeIKAnalyzer extends Analyzer{
	
	private boolean useSmart;
	private Configuration cfg;
	
	public boolean useSmart() {
		return useSmart;
	}

	public void setUseSmart(boolean useSmart) {
		this.useSmart = useSmart;
	}

	/**
	 * IK分词器Lucene  Analyzer接口实现类
	 * 
	 * 默认细粒度切分算法
	 */
	public RongkeIKAnalyzer(){
		this(false);
	}
	
	/**
	 * IK分词器Lucene Analyzer接口实现类
	 * 
	 * @param useSmart 当为true时，分词器进行智能切分
	 */
	public RongkeIKAnalyzer(boolean useSmart){
		super();
		this.useSmart = useSmart;
	}
	
	public RongkeIKAnalyzer(Configuration cfg) {
		super();
		this.cfg = cfg;
	}

	/**
	 * 重载Analyzer接口，构造分词组件
	 */
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		//Tokenizer _IKTokenizer = new IKTokenizer(this.useSmart());
		Tokenizer _IKTokenizer = new IKTokenizer(this.cfg);
		return new TokenStreamComponents(_IKTokenizer);
	}

}
