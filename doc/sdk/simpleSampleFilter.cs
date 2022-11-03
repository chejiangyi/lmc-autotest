
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc.Filters;
using Newtonsoft.Json;
using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.IO;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace autotest_simple_sdk
{
	/**
	 * C# simple-sdk
	 * autotest 应用使用/api/samples 拦截样本上传
	 * 注:未测试,simple版本不建议使用在大流量录制场景。
	 * by 车江毅
	 */
	public class AutoTestSimpleSampleFilter : IAsyncActionFilter
	{
		//最大缓存数量
		public static int MaxCacheSize = 5000;
		//autotest api的url地址
		public static String AutoTestApiUrl = "http://localhost:8080/api/samples/";
		//autotest 用户名
		public static String UserName = "admin";
		//autotest token秘钥
		public static String Token = "dfadfadfasdfasdfasdfadf";
		//autotest 应用名
		public static String AppName = "CSharp-default";

		//请求对象
		private class Request
		{
			public String appName;
			public String url;
			public String header;
			public String body;
			public String method;
			public DateTime createTime;
			//{"app_name":"test","url":"http://www.baidu.com","header":"{}","body":"{}","method":"GET"}
		}

		private static ConcurrentStack<Request> requests = new ConcurrentStack<Request>();
		private static object locked = new object();
		private static bool init = false;
		public async Task OnActionExecutionAsync(ActionExecutingContext context,
			ActionExecutionDelegate next)
		{
			if (context.HttpContext.Request.ContentType.ToLower() == "application/json") {
				//操作Request.Body之前加上EnableBuffering即可
				context.HttpContext.Request.EnableBuffering();
				using (StreamReader stream = new StreamReader(context.HttpContext.Request.Body)) {
					var r = context.HttpContext.Request;
					string body = await stream.ReadToEndAsync();
					Request request = new Request();
					request.method = r.Method;
					request.appName = AppName;
					request.header = JsonConvert.SerializeObject(r.Headers);
					request.body = body;
					request.url = GetCompleteUrl(r);
					request.createTime = DateTime.Now;
					Add(request);
				}
				context.HttpContext.Request.Body.Seek(0, SeekOrigin.Begin);
			}

		}

		/// <summary>
		/// 获取当前请求完整的Url地址
		/// </summary>
		/// <returns></returns>
		private string GetCompleteUrl(HttpRequest request)
		{
			return new StringBuilder()
				 .Append(request.Scheme)
				 .Append("://")
				 .Append(request.Host)
				 .Append(request.PathBase)
				 .Append(request.Path)
				 .Append(request.QueryString)
				 .ToString();
		}

		private void Add(Request request) {
			if (requests.Count > MaxCacheSize) {
				return;
			}
			requests.Push(request);
			if (init == false)
			{
				lock (locked)
				{
					if (init == false)
					{
						System.Threading.ThreadPool.QueueUserWorkItem((o) =>
						{
							while (true)
							{
								try
								{
									var cached = requests.ToArray();
									requests.Clear();
									if (cached.Length > 0)
									{
										Dictionary<String, String> form = new Dictionary<string, string>();
										form.Add("requests", JsonConvert.SerializeObject(cached));
										form.Add("username", UserName);
										form.Add("token", Token);
										HttpPostForm(AutoTestApiUrl, form);
									}
								}
								catch (Exception ex)
								{
									Console.WriteLine("【警告】录制流量失败：" + ex.Message);
								}
								try { System.Threading.Thread.Sleep(5000); } catch (Exception e) { }
							}
						});
						init = true;
					}
				}
			}
		}

		private void HttpPostForm(string url, Dictionary<String,String> data)
		{
			List<KeyValuePair<string, string>> param = new List<KeyValuePair<string, string>>();
			foreach (var d in data) {
				param.Add(new KeyValuePair<string, string>(d.Key, Convert.ToString(d.Value)));
			}
			using (var httpClient = new HttpClient())
			{
				var r = httpClient.PostAsync(url, new FormUrlEncodedContent(param));
			    r.Wait();
			}
		}
	}
}
