package RegressionTest.ListUtility;

import java.util.List;

import org.opentdk.api.util.ListUtil;

import RegressionTest.BaseRegression;

import java.util.ArrayList;

public class RT_ListUtility_asString extends BaseRegression {
	
	public static void main(String[] args) {
		new RT_ListUtility_asString();
	}
	
	@Override
	public void runTest() {
		List<String> list = new ArrayList<>();
		list.add("Test");
		list.add("1");
		list.add("%");
		String string = ListUtil.asString(list, ";");
		testResult(list.size(), "Size", string.split(";").length);
	}
}
