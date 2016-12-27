package com.nfury.liulian;

import java.util.Locale;
import com.unity3d.player.UnityPlayerActivity;
import com.unity3d.player.UnityPlayer;
import android.util.Log;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.cd.ll.game.sdk.SDKCallBackListener;
import com.cd.ll.game.sdk.SDKCallbackListenerNullException;
import com.liulian.game.sdk.LiuLianOrientation;
import com.liulian.game.sdk.LiulianSdkSetting;
import com.liulian.game.sdk.SDKStatusCode;
import com.liulian.game.sdk.SdkManager;
import com.liulian.game.sdk.data.bean.GameRoleInfo;

public class MainActivity extends UnityPlayerActivity{
	private static String UNITY_LOG_TAG = "Unity";
	private static boolean isNorLogin = true;
	private static String objectCallback = "";
	private static String functionCallback = "";
	private static Activity currentActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentActivity = this;
	}

	// 初始化榴莲SDK
	public static void initSDK(String appId, String appPubKey, String appPriKey, boolean isDebug, String objCallback, String fnCallback) {
		try {
			LiulianSdkSetting liulianSdkSetting = new LiulianSdkSetting();
			// 设置榴莲 APPID
			// liulianSdkSetting.setAppid("1a68f92073545fd9182ead579f4572e5");
			liulianSdkSetting.setAppid(appId);

			// 设置榴莲 APPKEY
			// liulianSdkSetting.setAppkey("cf205064372477031a21c4af2f568f0e");
			liulianSdkSetting.setAppkey(appPubKey);

			// 设置 PRIVATEKEY
			// liulianSdkSetting.setPrivateKey("b345f009d1e0a1a70b75ffb38f86b6e4");
			liulianSdkSetting.setPrivateKey(appPriKey);

			// 设置榴莲 SDK 调试模式是否开启
			// 设置true表示目前在测试环境中，正式上线时请修改为false
			// liulianSdkSetting.setDEBUG(true);
			liulianSdkSetting.setDEBUG(isDebug);
			MainActivity.objectCallback = objCallback;
			MainActivity.functionCallback = fnCallback;
			// LiuLianOrientation.PORTRAIT 竖屏
			// LiuLianOrientation.LANDSCAPE 横屏
			liulianSdkSetting.setOrientation(LiuLianOrientation.LANDSCAPE);
			Log.d(UNITY_LOG_TAG, "initSDK " +  appId + " "  + appPubKey + " " + appPriKey + " "+ isDebug + " " + objCallback);
			SdkManager.defaultSDK().initSDK(currentActivity, liulianSdkSetting,
					new SDKCallBackListener() {
						@Override
						public void callBack(int code, String msg) {
							switch (code) {
							case SDKStatusCode.INIT_SUCC:
								// 初始化成功
								// showToast("INIT_SUCC: msg=" + msg);
								sendUnityMessage(msg);
								// 登录
								break;
							case SDKStatusCode.INIT_FAIL:
								// 初始化失败
								// showToast("INIT_FAIL: msg=" + msg);
								sendUnityMessage(msg);
								break;
							default:
								// 初始化失败（其它错误）
								String pMsg = String.format(
										Locale.getDefault(),
										"Default：code=%d _ msg=%s", code, msg);
								// showToastLong(pMsg);
								sendUnityMessage(pMsg);
								break;
							}
						}
					});
		} catch (SDKCallbackListenerNullException e) {
			Log.d(UNITY_LOG_TAG, e.getMessage());
		}

		// 当用户登出监听回调
		SdkManager.defaultSDK().setOnLogoutListener(new SDKCallBackListener() {
			@Override
			public void callBack(int code, String msg) {
				if (code == SDKStatusCode.LOGOUT) {
					showToast("游戏登出了，cp主动再调起登录");
					doLogin();
				}
			}
		});
	}

	// 登录
	public static void doLogin() {
		isNorLogin = true;
		Log.d(UNITY_LOG_TAG, "do Login");
		try {
			SdkManager.defaultSDK().login(currentActivity,
					new SDKCallBackListener() {
						@Override
						public void callBack(int code, String msg) {
							switch (code) {
							case SDKStatusCode.LOGIN_SUCCESS:
								// 登录成功，返回相关数据(json)，游戏进行后续操作
								// msg 为 为 json 数据格式，解析可以获得 sid 值，sid 传递给游戏服务器
								// 最后由游戏服务器与榴莲服务器验证可获得玩家基本信息

								// if (isNorLogin) {
								// 	// 正常登录
								// 	isNorLogin = false;
								// 	showToastLong("登录成功：msg=" + msg);
								// 	// 显示悬浮窗
								// 	SdkManager.defaultSDK().showFloatingButton(
								// 			MainActivity.currentActivity);
								// 	// 上传角色
								// 	doGameRole();
								// } else {
								// 	// 切换账号回调
								// 	showToastLong("切换账号成功：msg=" + msg);
								// }
								sendUnityMessage(code + " " + msg);
								break;
							case SDKStatusCode.LOGIN_CANCEL:
								// 取消登录
								sendUnityMessage(msg);
								break;
							default:
								// 登录错误，判断相关信息
								String pMsg = String.format(
										Locale.getDefault(),
										"登录失败：code=%d _ msg=%s", code, msg);
								// showToastLong(pMsg);
								sendUnityMessage(pMsg);
								break;
							}
						}
					});
		} catch (SDKCallbackListenerNullException e) {
			Log.d(UNITY_LOG_TAG, e.getMessage());
		}
	}

	// 上传角色信息
	public static void doGameRole() {
		try {
			GameRoleInfo gameRoleInfo = new GameRoleInfo();
			gameRoleInfo.setServerId("12873");
			gameRoleInfo.setServerName("测试服务器");
			gameRoleInfo.setRoleId("1001");
			gameRoleInfo.setRoleName("山财");
			gameRoleInfo.setRoleLevel("52");
			gameRoleInfo.setRoleCTime((System.currentTimeMillis() / 1000));
			SdkManager.defaultSDK().reportGameRole(MainActivity.currentActivity,
					gameRoleInfo, new SDKCallBackListener() {
						@Override
						public void callBack(int code, String msg) {
							switch (code) {
							case SDKStatusCode.GAME_ROLE_SUCCESS:
								Toast.makeText(MainActivity.currentActivity,
										"角色上传成功 :" + msg, Toast.LENGTH_LONG)
										.show();
								break;
							case SDKStatusCode.GAME_ROLE_FAIL:
								Toast.makeText(MainActivity.currentActivity,
										"角色上传失败:" + msg, Toast.LENGTH_LONG)
										.show();
								break;
							}

						}
					});
		} catch (SDKCallbackListenerNullException e) {
			e.printStackTrace();
		}
	}

	// 支付
	public static void doPay(float money) {
		// 商品名称
		String productName = "商品名称";
		// 玩家充值服务器ID
		String serverId = "1";
		// 玩家角色ID
		String roleId = "15001424";
		// 玩家角色名
		String roleName = "iWgang";
		// 透传数据,SDK不做任何处理会通过服务器充值回调接口直接返回给游戏服务器
		String extInfo = "透传数据563072113f650a589c8fe31c";
		try {
			SdkManager.defaultSDK().pay(currentActivity, money, productName, serverId,
					roleId, roleName, extInfo, new SDKCallBackListener() {
						@Override
						public void callBack(int code, String msg) {
							switch (code) {
							case SDKStatusCode.PAY_SUCCESS:
								// 支付成功
								showToast("支付成功");
								break;
							case SDKStatusCode.PAY_ERROR:
								showToastLong("支付失败：msg=" + msg);
								break;
							case SDKStatusCode.PAY_CANCEL:
								showToast("支付已取消");
								break;
							}
						}
					});
		} catch (SDKCallbackListenerNullException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		SdkManager.defaultSDK().onNewIntent(currentActivity, intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		SdkManager.defaultSDK().onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onStart() {
		super.onStart();
		SdkManager.defaultSDK().onStart(currentActivity);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SdkManager.defaultSDK().onResume(currentActivity);
	}

	@Override
	protected void onPause() {
		super.onPause();
		SdkManager.defaultSDK().onPause(currentActivity);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		SdkManager.defaultSDK().onRestart(currentActivity);
	}

	@Override
	protected void onStop() {
		super.onStop();
		SdkManager.defaultSDK().onStop(currentActivity);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SdkManager.defaultSDK().onDestroy(currentActivity);
	}

	private static void showToast(String msg) {
		Toast.makeText(MainActivity.currentActivity, msg, Toast.LENGTH_SHORT).show();
	}

	private static void showToastLong(String msg) {
		Toast.makeText(MainActivity.currentActivity, msg, Toast.LENGTH_LONG).show();
	}

	private static void sendUnityMessage(String message) {
		Log.d(UNITY_LOG_TAG, "sendUnityMessage " + objectCallback + ":" + functionCallback + " " + message);
		UnityPlayer.UnitySendMessage(objectCallback, functionCallback, message);
	}

	@Override
	public void onBackPressed() {
		SdkManager.defaultSDK().exitGame(currentActivity, new SDKCallBackListener() {
			@Override
			public void callBack(int code, String msg) {
				switch (code) {
				case SDKStatusCode.GAME_EXIT_SUCCESS:
					// 真正退出
					finish();
					android.os.Process.killProcess(android.os.Process.myPid());
					break;
				case SDKStatusCode.GAME_EXIT_UNTREATED:
					// TODO SDK没有处理退出，游戏可以直接finish()或者用自己的退出dialog
					new AlertDialog.Builder(MainActivity.currentActivity)
							.setMessage("是否退出游戏？")
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}

									})
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											MainActivity.currentActivity.finish();
											android.os.Process
													.killProcess(android.os.Process
															.myPid());
										}
									}).create().show();
					break;
				case SDKStatusCode.GAME_EXIT_CANCEL:
					// 取消退出
					break;
				}
			}
		});
	}
}
