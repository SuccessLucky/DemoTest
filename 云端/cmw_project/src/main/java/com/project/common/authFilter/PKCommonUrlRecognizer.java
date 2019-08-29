package com.project.common.authFilter;

public class PKCommonUrlRecognizer {

	protected boolean matchURLInAry(String url, String[] ary) {
		for (String str : ary) {
			if (str.length() > 0 && url.contains(str))
				return true;
		}

		return false;
	}

	protected boolean isUrlAction(String url) {
		String[] actionSuffix = SysEnvHelper.getActionSuffix();

		for (String suffix : actionSuffix) {
			String str = "." + suffix;

			if (url.toLowerCase().indexOf(str) != -1)
				return true;
		}

		return false;
	}

}
