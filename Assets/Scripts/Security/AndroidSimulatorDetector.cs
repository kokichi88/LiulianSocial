using UnityEngine;
using System.Collections;

public class AndroidSimulatorDetector : IDetector {
	private AndroidJavaClass clazz;

	public AndroidSimulatorDetector()
	{
		clazz = new AndroidJavaClass("net.skoumal.emulatordetector.EmulatorDetector");
	}

	public bool IsFound()
	{
		return clazz.CallStatic<bool>("deviceIsEmulator");
	}

	public string GetDescription()
	{
		return clazz.CallStatic<string>("getDeviceListing");
	}


}
