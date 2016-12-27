using UnityEngine;
using System.Collections;
using System;

public sealed class LiuLian  {
	private static AndroidJavaObject sdkImpl;
	private static bool isInitiated = false;

	public static void Init(string appId, string appPubKey, string appPriKey, bool isDebug, string objectCallback, string fnCallback)
	{
		try{
			if(isInitiated) return;
			isInitiated = true;
			AndroidJavaClass cls_UnityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
			sdkImpl = cls_UnityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
			object[] args = {appId, appPubKey, appPriKey, isDebug, objectCallback, fnCallback};
			sdkImpl.Call("initSDK", args);
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
			sdkImpl.Call("doLogin");
		}else
		{
			throw new Exception("You have to initiate sdk before Login");
		}
	}

	public static void Exit()
	{
		if(isInitiated)
		{
			sdkImpl.Call("doExit");
		}else
		{
			throw new Exception("You have to initiate sdk before Exit");
		}
	}

	public static void ReportGameRole(string sid, string sname, string uid, string uname, string ulevel)
	{
		if(isInitiated)
		{
			object[] args = {sid, sname, uid, uname, ulevel};
			sdkImpl.Call("reportGameRole", args);
		}else
		{
			throw new Exception("You have to initiate sdk before ReportGameRole");
		}
	}

	public static void Buy(string pname, string sid, string uid, string uname, float money)
	{
		if(isInitiated)
		{
			object[] args = {pname, sid, uid, uname, money};
			sdkImpl.Call("doBuy", args);
		}else
		{
			throw new Exception("You have to initiate sdk before Buy");
		}
	}
}
