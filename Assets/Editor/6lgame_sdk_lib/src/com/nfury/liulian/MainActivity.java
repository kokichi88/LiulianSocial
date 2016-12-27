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

public class MainActivity extends UnityPlayerActivity {
	private static String UNITY_LOG_TAG = "Unity";
	private boolean isNotLogin = true;
	private String objectCallback = "";
	private String functionCallback = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	// 初始化榴莲SDK
	public void initSDK(String appId, String appPubKey, String appPriKey, boolean isDebug, String objCallback, String fnCallback) {
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
			objectCallback = objCallback;
			functionCallback = fnCallback;
			// LiuLianOrientation.PORTRAIT 竖屏
			// LiuLianOrientation.LANDSCAPE 横屏
			liulianSdkSetting.setOrientation(LiuLianOrientation.LANDSCAPE);
			Log.d(UNITY_LOG_TAG, "initSDK " +  appId + " "  + appPubKey + " " + appPriKey + " "+ isDebug + " " + objCallback);
			SdkManager.defaultSDK().initSDK(this, liulianSdkSetting,
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
	public void doLogin() {
		isNotLogin = true;
		Log.d(UNITY_LOG_TAG, "do Login");
		this.runOnUiThread(new Runnable() {
		    public void run() {
		        try {
					SdkManager.defaultSDK().login(MainActivity.this,
							new SDKCallBackListener() {
								@Override
								public void callBack(int code, String msg) {
									switch (code) {
									case SDKStatusCode.LOGIN_SUCCESS:
										if (isNotLogin) {
											// 正常登录
											isNotLogin = false;
											showToastLong("LOGIN_SUCCESS：msg=" + msg);
											// 显示悬浮窗
											SdkManager.defaultSDK().showFloatingButton(
													MainActivity.this);
										} else {
											// 切换账号回调
											showToastLong("ALREADY LOGIN：msg=" + msg);
										}
										sendUnityMessage(code + " " + msg);
										break;
									case SDKStatusCode.LOGIN_CANCEL:
										// 取消登录
										showToastLong("LOGIN_CANCEL=" + msg);
										sendUnityMessage(msg);
										break;
									default:
										// 登录错误，判断相关信息
										String pMsg = String.format(
												Locale.getDefault(),
												"DEFAULT：code=%d _ msg=%s", code, msg);
										showToastLong(pMsg);
										sendUnityMessage(pMsg);
										break;
									}
								}
							});
				} catch (SDKCallbackListenerNullException e) {
					Log.d(UNITY_LOG_TAG, e.getMessage());
				}
		    }
		});
		
	}

	public void doExit() {
		Log.d(UNITY_LOG_TAG, "do Exit");
		this.runOnUiThread(new Runnable() {
		    public void run() {
		    	SdkManager.defaultSDK().exitGame(MainActivity.this, new SDKCallBackListener() {
					@Override
					public void callBack(int code, String msg) {
						switch (code) {
						case SDKStatusCode.GAME_EXIT_SUCCESS:
							// 真正退出
							// finish();
							// android.os.Process.killProcess(android.os.Process.myPid());
							showToast("SDKStatusCode.GAME_EXIT_SUCCESS " + msg);
							break;
						case SDKStatusCode.GAME_EXIT_UNTREATED:
							// TODO SDK没有处理退出，游戏可以直接finish()或者用自己的退出dialog
							showToast("SDKStatusCode.GAME_EXIT_UNTREATED " + msg);
							new AlertDialog.Builder(MainActivity.this)
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
													MainActivity.this.finish();
													android.os.Process
															.killProcess(android.os.Process
																	.myPid());
												}
											}).create().show();
							break;
						case SDKStatusCode.GAME_EXIT_CANCEL:
							// 取消退出
							showToast("SDKStatusCode.GAME_EXIT_CANCEL");
							break;
						}
					}
				});
		    }
		});
		
	}


	// 上传角色信息
	public void reportGameRole(String sid, String sname, String uid, String uname, String ulevel) {
		Log.d(UNITY_LOG_TAG, "reportGameRole " + sid + " " + sname + " " + uid + " " + uname + " " + ulevel);
		try {
			GameRoleInfo gameRoleInfo = new GameRoleInfo();
			gameRoleInfo.setServerId(sid);
			gameRoleInfo.setServerName(sname);
			gameRoleInfo.setRoleId(uid);
			gameRoleInfo.setRoleName(uname);
			gameRoleInfo.setRoleLevel(ulevel);
			gameRoleInfo.setRoleCTime((System.currentTimeMillis() / 1000));
			SdkManager.defaultSDK().reportGameRole(MainActivity.this,
					gameRoleInfo, new SDKCallBackListener() {
						@Override
						public void callBack(int code, String msg) {
							switch (code) {
							case SDKStatusCode.GAME_ROLE_SUCCESS:
								Toast.makeText(MainActivity.this,
										"GAME_ROLE_SUCCESS:" + msg, Toast.LENGTH_LONG)
										.show();
								break;
							case SDKStatusCode.GAME_ROLE_FAIL:
								Toast.makeText(MainActivity.this,
										"GAME_ROLE_FAIL:" + msg, Toast.LENGTH_LONG)
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
	public void doBuy(final String pname, final String sid, final String uid, final String uname, final float money) {
		Log.d(UNITY_LOG_TAG, "doBuy " + pname + " " + sid + " " + uid + " " + uname + " " + money);
		// 透传数据,SDK不做任何处理会通过服务器充值回调接口直接返回给游戏服务器
		final String extInfo = "透传数据563072113f650a589c8fe31c";
		this.runOnUiThread(new Runnable() {
		    public void run() {
		    	try {
					SdkManager.defaultSDK().pay(MainActivity.this, money, pname, sid,
							uid, uname, extInfo, new SDKCallBackListener() {
								@Override
								public void callBack(int code, String msg) {
									switch (code) {
									case SDKStatusCode.PAY_SUCCESS:
										// 支付成功
										showToast("PAY_SUCCESS " + msg);
										break;
									case SDKStatusCode.PAY_ERROR:
										showToastLong("PAY_ERROR：msg=" + msg);
										break;
									case SDKStatusCode.PAY_CANCEL:
										showToast("PAY_CANCEL " + msg);
										break;
									}
								}
							});
				} catch (SDKCallbackListenerNullException e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		SdkManager.defaultSDK().onNewIntent(this, intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		SdkManager.defaultSDK().onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onStart() {
		super.onStart();
		SdkManager.defaultSDK().onStart(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SdkManager.defaultSDK().onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		SdkManager.defaultSDK().onPause(this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		SdkManager.defaultSDK().onRestart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		SdkManager.defaultSDK().onStop(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SdkManager.defaultSDK().onDestroy(this);
	}

	private void showToast(String msg) {
		Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
	}

	private void showToastLong(String msg) {
		Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
	}

	private void sendUnityMessage(String message) {
		Log.d(UNITY_LOG_TAG, "sendUnityMessage " + objectCallback + ":" + functionCallback + " " + message);
		UnityPlayer.UnitySendMessage(objectCallback, functionCallback, message);
	}

}
