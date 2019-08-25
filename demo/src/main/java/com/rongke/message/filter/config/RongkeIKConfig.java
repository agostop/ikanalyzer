package com.rongke.message.filter.config;

import com.rongke.analyzer.cfg.Configuration;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class RongkeIKConfig implements Configuration {

	private final Logger m_logger = Logger.getLogger(RongkeIKConfig.class);
	/*
	 * 分词器默认字典路径 
	 */
	private final String patchDicMain = "org/wltea/analyzer/dic/main2012.dic";
	private final String pathDicQuantifier = "org/wltea/analyzer/dic/quantifier.dic";

	/*
	 * 分词器配置文件路径
	 */	
	private String configFileName;

	//配置属性——扩展字典
	private String extDictCfg ;

	//配置属性——扩展停止词典
	private String extStopWordDictCfg;

    /*
	 * 是否使用smart方式分词
	 */
	private boolean useSmart;
	
	/*
	 * 初始化配置文件
	 */
	public RongkeIKConfig(String configPath) {
        this.configFileName = configPath;
	}
	
	/**
	 * 返回useSmart标志位
	 * useSmart =true ，分词器使用智能切分策略， =false则使用细粒度切分
	 * @return useSmart
	 */
	@Override
	public boolean useSmart() {
		return useSmart;
	}

	/**
	 * 设置useSmart标志位
	 * useSmart =true ，分词器使用智能切分策略， =false则使用细粒度切分
	 * @param useSmart
	 */
	@Override
	public void setUseSmart(boolean useSmart) {
		this.useSmart = useSmart;
	}	
	
	/**
	 * 获取主词典路径
	 * 
	 * @return String 主词典路径
	 */
	@Override
	public String getMainDictionary(){
		return patchDicMain;
	}

	/**
	 * 获取量词词典路径
	 * @return String 量词词典路径
	 */
	@Override
	public String getQuantifierDicionary(){
		return pathDicQuantifier;
	}

	/**
	 * 获取扩展字典配置路径
	 * @return List<String> 相对类加载器的路径
	 */
	@Override
	public List<String> getExtDictionarys(){
		List<String> extDictFiles = new ArrayList<String>(2);
		if(this.extDictCfg != null){
			//使用;分割多个扩展字典配置
			String[] filePaths = this.extDictCfg.split(";");
            for(String filePath : filePaths){
                if(filePath != null && !"".equals(filePath.trim())){
                    String syspath = new File(this.configFileName).getParent();
                    extDictFiles.add(syspath + File.separatorChar + filePath.trim());
                }
            }
        }
		return extDictFiles;		
	}

	/**
	 * 获取扩展停止词典配置路径
	 * @return List<String> 相对类加载器的路径
	 */
	@Override
	public List<String> getExtStopWordDictionarys(){
		List<String> extStopWordDictFiles = new ArrayList<String>(2);
		if(this.extStopWordDictCfg != null){
			//使用;分割多个扩展字典配置
			String[] filePaths = this.extStopWordDictCfg.split(";");
            for(String filePath : filePaths){
                if(filePath != null && !"".equals(filePath.trim())){
                    extStopWordDictFiles.add(filePath.trim());
                }
            }
        }
		return extStopWordDictFiles;		
	}

	@Override
	public List<String> getExtDirctionarysContent() {
		return null;
	}

	public String getExtDictCfg() {
        return extDictCfg;
    }

    public void setExtDictCfg(String extDictCfg) {
        this.extDictCfg = extDictCfg;
    }

    public String getExtStopWordDictCfg() {
        return extStopWordDictCfg;
    }

    public void setExtStopWordDictCfg(String extStopWordDictCfg) {
        this.extStopWordDictCfg = extStopWordDictCfg;
    }
}
