using UnityEngine;
using System.Collections;
using System;
using System.Collections.Generic;
namespace nfury.liulian
{
	public sealed class LiuLian  {
		private static AndroidJavaObject sdkImpl;
		private static bool isInitiated = false;
		public static Dictionary<int,HandlerMessage> handlers = new Dictionary<int, HandlerMessage>(); 
		
		private static event HandlerMessage sdkInitSuccessHandler;
		private static event HandlerMessage sdkInitFailureHandler;
		
		private static event HandlerMessage loginSuccessHandler;
		private static event HandlerMessage loginCancelHandler;
		private static event HandlerMessage loginFailureHandler;
		
		private static event HandlerMessage logoutHandler;
		
		private static event HandlerMessage exitSuccessHandler;
		private static event HandlerMessage exitUntreatedHandler;
		private static event HandlerMessage exitCancelHander;
		
		private static event HandlerMessage reportGameRoleSuccessHandler;
		private static event HandlerMessage reportGameRoleFailureHandler;
		
		private static event HandlerMessage buySuccessHandler;
		private static event HandlerMessage buyFailureHandler;
		private static event HandlerMessage buyCancelHandler;
		
		public delegate void HandlerMessage(string msg);
		
		
		static LiuLian()
		{
			handlers[SDKStatusCode.INIT_SUCC] = sdkInitSuccessHandler; 
			handlers[SDKStatusCode.INIT_FAIL] = sdkInitFailureHandler; 
			
			handlers[SDKStatusCode.LOGIN_SUCCESS] = loginSuccessHandler; 
			handlers[SDKStatusCode.LOGIN_ERROR] = loginFailureHandler; 
			handlers[SDKStatusCode.LOGIN_CANCEL] = loginCancelHandler; 
			
			handlers[SDKStatusCode.LOGOUT] = logoutHandler; 
			
			handlers[SDKStatusCode.GAME_EXIT_SUCCESS] = exitSuccessHandler; 
			handlers[SDKStatusCode.GAME_EXIT_UNTREATED] = exitUntreatedHandler;
			handlers[SDKStatusCode.GAME_EXIT_CANCEL] = exitCancelHander; 
			
			handlers[SDKStatusCode.GAME_ROLE_SUCCESS] = reportGameRoleSuccessHandler; 
			handlers[SDKStatusCode.GAME_ROLE_FAIL] = reportGameRoleFailureHandler; 
			
			handlers[SDKStatusCode.PAY_SUCCESS] = buySuccessHandler; 
			handlers[SDKStatusCode.PAY_ERROR] = buyFailureHandler; 
			handlers[SDKStatusCode.PAY_CANCEL] = buyCancelHandler; 
		}
		
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
		
		public static void Buy(string pname, string sid, string uid, string uname, float money, string payload)
		{
			if(isInitiated)
			{
				object[] args = {pname, sid, uid, uname, money, payload};
				sdkImpl.Call("doBuy", args);
			}else
			{
				throw new Exception("You have to initiate sdk before Buy");
			}
		}
	}
}

