package RegressionTests.ListUtility;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.opentdk.api.util.ListUtil;

import java.util.ArrayList;

public class RT_ListUtility_asString {

	@Test
	public void stringListToString() {
		List<String> list = new ArrayList<>();
		list.add("Test");
		list.add("1");
		list.add("%");
		String string = ListUtil.asString(list, ";");
		Assert.assertEquals(list.size(), string.split(";").length);
	}
}
