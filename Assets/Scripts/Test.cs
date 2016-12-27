using UnityEngine;
using System.Collections;

public class Test : MonoBehaviour {
	string activityName = "NONE";
	void OnGUI()
	{
		if(GUI.Button(new Rect(0,0,Screen.width, 100), "Init")){
			Init();
		}

		if(GUI.Button(new Rect(0,100,Screen.width, 100), "Login")){
			Login();
		}


		GUI.Label(new Rect(0,Screen.height/2, Screen.width, 100), activityName);
	}

	void Init()
	{
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

	public void Callback(string message)
	{
		Debug.Log("Callback from android " + message);
	}
}
