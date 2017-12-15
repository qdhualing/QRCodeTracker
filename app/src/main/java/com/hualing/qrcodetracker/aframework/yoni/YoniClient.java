package com.hualing.qrcodetracker.aframework.yoni;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hualing.qrcodetracker.aframework.util.Logger;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class YoniClient {
	private final String TAG = "YoniClient";
	private static YoniClient instance;
	private OkHttpClient okHttpClient;
	private String baseUrl;
	private String resourceUrl;
	private boolean debug = false;
	private String user;
	private List<Interceptor> interceptors=new ArrayList<>();
	private YoniClient() {
		this.okHttpClient = new OkHttpClient();
	}

	public void setBaseUrl(String url) {
		this.baseUrl = url;
	}

	public void setResourceUrl(String url) {
		this.resourceUrl = url;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void addInterceptor(Interceptor interceptor){
		this.interceptors.add(interceptor);
	}
	public static YoniClient getInstance() {
		if (instance == null) {
			instance = new YoniClient();
		}
		return instance;
	}

	public NetResponse request(RequestParams params, Class<?> resultClass) {
		return request(null, params, resultClass);
	}

	public NetResponse request(String url, RequestParams params,
															   Class<?> resultClass) {
		NetResponse nr = new NetResponse();
		if (url == null || url.length() == 0) {
			url = baseUrl;
		}
		try {
			for (Interceptor interceptor:interceptors){
				boolean res=interceptor.intercept(params,nr);
				if (!res){
					return  nr;
				}
			}
			String requestString = JSON.toJSONString(params);
			if (debug) {
				Logger.d(TAG, requestString);
			}
			RequestBody body = new FormEncodingBuilder().add("json",
					requestString).build();

			Request request = new Request.Builder().url(url).post(body).build();
			Response response = okHttpClient.newCall(request).execute();
			String responseString = response.body().string();
			if (debug) {
				Logger.d(TAG, responseString);
			}
			if (response.isSuccessful()) {

				nr = JSON.parseObject(responseString, NetResponse.class);
				if (nr.getResult() != null && resultClass != null) {

					if (nr.getResult() instanceof JSONArray) {
						List<Object> result = new ArrayList<Object>();
						for (int i = 0; i < ((JSONArray) nr.getResult()).size(); i++) {
							JSONObject jsonObject = (JSONObject) ((JSONArray) nr
									.getResult()).get(i);
							Object object = JSON.toJavaObject(jsonObject,
									resultClass);
							result.add(object);
						}
						nr.setResult(result);
					} else {
						Object result = JSON.toJavaObject(
								(JSONObject) nr.getResult(), resultClass);
						nr.setResult(result);
					}
				}
			} else {
				nr.setCode(NetResponse.STATUS_ERROR_CONNECT);
				nr.setMessage("网络数据请求失败");
			}

		} catch (JSONException e1) {
			nr.setCode(NetResponse.STATUS_ERROR_PARAMS);
			nr.setMessage("参数解析失败");
			e1.printStackTrace();
		} catch (Exception e2) {
			nr.setCode(NetResponse.STATUS_ERROR_IO);
			nr.setMessage("网络数据请求失败");
			e2.printStackTrace();
		}
		return nr;
	}

	public void uploadFile(final String filePath, String fileType,
			final ResultCallBack callBack) {
		uploadFile(null, filePath, fileType, callBack);
	}

	public void uploadFile(String url, final String filePath, String fileType,
			final ResultCallBack callBack) {
		if (url == null || url.length() == 0) {
			url = resourceUrl;
		}
		File file = new File(filePath);
		String exName = getFileExtension(file);
		String fileName = file.getName();
		RequestBody fileBody = RequestBody.create(
				MediaType.parse("application/octet-stream"), file);
		RequestBody requestBody = new MultipartBuilder()
				.type(MultipartBuilder.FORM).addFormDataPart("exName", exName)
				.addFormDataPart("fileType", fileType)
				.addFormDataPart("file", fileName, fileBody).build();
		Request request = new Request.Builder().url(url).post(requestBody)
				.build();
		Call call = okHttpClient.newCall(request);
		call.enqueue(new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {

				String url = arg0.body().string();
				callBack.onComplate(filePath, url);
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {

				callBack.onFailure(filePath);
			}
		});
	}

	private String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		} else {
			return "";
		}
	}

	public interface ResultCallBack {
		void onComplate(String filePath, String url);

		void onFailure(String filePath);
	}

	Map<Class<?>, Object> serviceMap;

	@SuppressWarnings("unchecked")
	public <T> T create(final Class<T> service) {
		if (serviceMap == null) {
			serviceMap = new HashMap<>();
		}
		T proxyService = null;
		if (serviceMap.containsKey(service)) {
			proxyService = (T) serviceMap.get(service);
		} else {
			proxyService = (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service }, new InvocationHandler() {

				@Override
				public Object invoke(Object proxy,java.lang.reflect.Method method, Object[] args) throws Throwable {
					ActionRequest anno = method.getAnnotation(ActionRequest.class);
					if (anno == null) {
						return method.invoke(proxy, args);
					} else {
						Type t = method.getGenericReturnType();
						Type[] types = ((ParameterizedType) t).getActualTypeArguments();
						Class<?> resultClass = (Class<?>) types[0];
						RequestParams serviceParams = new RequestParams();
						serviceParams.setFunc(anno.func());
						if (anno.user() == null || anno.user().length() == 0) {
							serviceParams.setUser(user);
						} else {
							serviceParams.setUser(anno.user());
						}
						if (args!=null&&args.length>0){
							serviceParams.setParams(args[0]);
						}
						NetResponse sr = request(serviceParams, resultClass);
						ActionResult<T> result = new ActionResult<>();
						result.setCode(sr.getCode());
						result.setMessage(sr.getMessage());
						if (sr.getResult() != null) {
							result.setResult((T) sr.getResult());
						}
						return result;
					}
				}
			});
			serviceMap.put(service, proxyService);
		}
		return proxyService;
	}
}
