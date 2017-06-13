package com.maximo.tools.impxml.config;

public class ImpXmlExcleConfig {
		private int titleCount=1;

		public int getTitleCount() {
			return titleCount;
		}

		public void setTitleCount(String titleCount) {
			if(titleCount!=null&&!titleCount.trim().isEmpty()){
				this.titleCount = Integer.parseInt(titleCount);
			}
		}
}
