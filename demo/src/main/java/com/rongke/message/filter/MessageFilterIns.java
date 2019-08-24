package com.rongke.message.filter;

import com.rongke.mcs.config.ConfigKey;
import com.rongke.mcs.dispatcher.Dispatcher;

import java.io.File;

public enum MessageFilterIns {
	
	INSTANCE;
	private final String m_sysPath = new File(Dispatcher.getInstance().getServletConfigPath()).getParent() + File.separatorChar;
	private final String m_analyzerConfig = m_sysPath + ConfigKey.FILTER_IKANALYZER_CONF;
	private final String m_idxDir = m_sysPath + ConfigKey.FILTER_INDEX_PATH;
	private final String m_senDic = m_sysPath + ConfigKey.FILTER_SENSITIVE_DICT;
	
	private MessageFilter m_msgFilter;

	private MessageFilterIns() {
		m_msgFilter = new MessageFilter(m_analyzerConfig, m_idxDir);
	}
	
	public boolean grep(String msg) {
		return m_msgFilter.grep(msg);
	}
	
	public void crtIdx() {
		
		File idxPath = new File(m_idxDir);
		if (!idxPath.exists()) {
			idxPath.mkdirs();
		}
			
		String[] fileList = idxPath.list();
		if (fileList.length == 0)
			m_msgFilter.crtDoc(m_senDic);
		
	}

}
