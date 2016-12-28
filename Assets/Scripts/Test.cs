using UnityEngine;
using System.Collections;
using nfury.liulian;

public class Test : MonoBehaviour {
	string output = "NONE";
	void OnGUI()
	{
		if(GUI.Button(new Rect(0,0,Screen.width, 100), "Init")){
			Init();
		}

		if(GUI.Button(new Rect(0,100,Screen.width, 100), "Login")){
			Login();
		}

		if(GUI.Button(new Rect(0,200,Screen.width, 100), "Exit")){
			Exit();
		}

		if(GUI.Button(new Rect(0,300,Screen.width, 100), "ReportGameRole")){
			ReportGameRole();
		}

		if(GUI.Button(new Rect(0,400,Screen.width, 100), "Buy 商品名称. Money = 1 ")){
			Buy();
		}

		if (Input.GetKeyUp(KeyCode.Escape))
		{
			if (Application.platform == RuntimePlatform.Android)
			{
				Exit();
			}
		}

		GUI.Label(new Rect(0,Screen.height/2, Screen.width, 100), output);
	}

	void Init()
	{
		LiuLian.handlers[SDKStatusCode.INIT_SUCC] += OnInitSuccess1;
		LiuLian.handlers[SDKStatusCode.INIT_SUCC] += OnInitSuccess2;

		string appId = "1a68f92073545fd9182ead579f4572e5";
		string appPubKey = "cf205064372477031a21c4af2f568f0e";
		string appPriKey = "b345f009d1e0a1a70b75ffb38f86b6e4";
		bool isDebug = true;
		string objCallback = "Test";
		string functionCallback = "Callback";
		LiuLian.Init(appId, appPubKey, appPriKey, isDebug, objCallback, functionCallback);


	}


	void Login ()
	{
		LiuLian.Login();
	}

	void Exit ()
	{
		LiuLian.Exit();
	}

	void ReportGameRole ()
	{
		string sid = "12873";
		string sname = "测试服务器";
		string uid = "1001";
		string uname = "山财";
		string ulevel = "52";
		LiuLian.ReportGameRole(sid, sname, uid, uname, ulevel);
	}

	void Buy ()
	{
		string pname = "商品名称";
		string sid = "1";
		string uid = "200000004156";
		string uname = "kokichi";
		float money = 1;
		string payload = Time.timeSinceLevelLoad.ToString();
		LiuLian.Buy(pname, sid, uid, uname, money, payload);
	}

	public void Callback(string json)
	{
		Debug.Log("Callback: " + json);
		output = json;
		LiuLianMsg msg = LitJson2.JsonMapper.ToObject<LiuLianMsg>(json);
		int code = msg.code;
		string message = msg.msg;
		if(LiuLian.handlers.ContainsKey(code))
		{
			LiuLian.HandlerMessage handler = LiuLian.handlers[code];
			if(handler != null)
				handler.Invoke(message);
		}
	}

	void OnInitSuccess1(string msg)
	{
		Debug.Log("OnInitSuccess1 >>> " + msg);
	}

	void OnInitSuccess2(string msg)
	{
		Debug.Log("OnInitSuccess2 >>> " + msg);
	}
}
