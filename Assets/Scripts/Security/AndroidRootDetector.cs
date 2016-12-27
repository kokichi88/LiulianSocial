using UnityEngine;
using System.Collections;

public class AndroidRootDetector : IDetector {
	private AndroidJavaClass clazz;

	public AndroidRootDetector()
	{
		clazz = new AndroidJavaClass("com.stericson.RootTools.RootTools");
	}

	public bool IsFound()
	{
		return clazz.CallStatic<bool>("isAccessGiven") || clazz.CallStatic<bool>("isRootAvailable");
	}

	public string GetDescription()
	{
		return "Check rooted device";
	}


}
