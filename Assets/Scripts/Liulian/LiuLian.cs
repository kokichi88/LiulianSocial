using UnityEngine;
using System.Collections;
using System;

public sealed class LiuLian  {
	private static AndroidJavaClass sdkImpl;
	private static bool isInitiated = false;

	public static void Init(string appId, string appPubKey, string appPriKey, bool isDebug, string objectCallback, string fnCallback)
	{
		try{
			if(isInitiated) return;
			isInitiated = true;
			sdkImpl = new AndroidJavaClass("com.nfury.liulian.MainActivity");
			object[] args = {appId, appPubKey, appPriKey, isDebug, objectCallback, fnCallback};
			sdkImpl.CallStatic("initSDK", args);
		}catch(Exception e)
		{
			isInitiated = false;
			Debug.LogError("cant load LiuLian sdk");
		}
	}

	public static void Login()
	{
		if(isInitiated)
		{
			sdkImpl.CallStatic("doLogin");
		}else
		{
			throw new Exception("You have to initiate sdk before login");
		}
	}
}
