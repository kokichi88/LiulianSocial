using UnityEngine;
using System.Collections;
using System;

public class TestDetector : MonoBehaviour {
	string retSimulator = "";
	string desc = "";
	// Use this for initialization
	void Start () {
		try{
			IDetector simulatorDetector = new AndroidSimulatorDetector();
			retSimulator = simulatorDetector.IsFound().ToString();
			desc = simulatorDetector.GetDescription();
		}catch(Exception e)
		{
			retSimulator = "Unsupported platform";
			desc = e.StackTrace;
		}

	}
	
	// Update is called once per frame
	void Update () {
		
	}

	void OnGUI()
	{
		GUI.Label(new Rect(Screen.width/2, Screen.height/2- 100,100,100), "test Simulator ret " + retSimulator);
		GUI.Label(new Rect(Screen.width/2, Screen.height/2,800,800), "test Simulator desc " + desc);
	}
}
