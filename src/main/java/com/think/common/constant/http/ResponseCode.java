package com.think.common.constant.http;

/**
 * 响应码枚举类
 * 
 * @author veione
 *
 */
public enum ResponseCode {
	SC_ACCEPTED(202), // 表示一个请求已经被接受处理，但还没有完成
	SC_BAD_GATEWAY(502), // 表明HTTP服务器从一个服务器收到了一个无效的响应，当其作为一个代理服务器或网关时，无法作出判断。
	SC_BAD_REQUEST(400), // 表示有客户端发出的请求在语法上是错误的。
	SC_CONFLICT(409), // 表示由于与当前资源状态的冲突，请求无法完成。
	SC_CONTINUE(100), // 表示客户端可以继续。
	SC_CREATED(201), // 表示请求已经完成，并在服务器上创建一个新的资源。
	SC_EXPECTATION_FAILED(417), // 表示服务器不能满足给定的Expect请求头中的期望。
	SC_FORBIDDEN(403), // 表明服务器能够理解请求但是拒绝履行之。
	SC_GATEWAY_TIMEOUT(504), // 表示服务器作为网关和代理服务器没有收到发自上游服务器的时间请求。
	SC_GONE(410), // 表示服务器上的资源不在有效，且没有已知的前转地址。
	SC_HTTP_VERSION_NOT_SUPPORTED(505), // 表示服务器不支持或拒绝支持在请求信息中使用的HTTP协议版本。
	SC_INTERNAL_SERVER_ERROR(500), // 指出在HTTP服务器中有一个错误，阻碍了其履行请求消息。
	SC_LENGTH_REQUIRED(411), // 表示由于没有一个定义的Content-Length，请求信息不能被处理。
	SC_METHOD_NOT_ALLOWED(405), // 表明在Request-Line 中指定的方法不支持由Request-URI标示的资源。
	SC_MOVED_PERMANENTLY(301), // 表示资源已经被永久地转移到一个新的地方，将来的参考应当用一个带有请求的新的URL。
	SC_MOVED_TEMPORARILY(302), // 表明资源已经被暂时地转移到一个新的地方，但将来的参考应当继续用初始的URL来访问资源。
	SC_MULTIPLE_CHOICES(300), // 表示被请求的资源与表达集合中的任何一个进行通信，每个都以自己明确的地址。
	SC_NO_CONTENT(204), // 表示请求已经完成，但却没有新的信息以供返回。
	SC_NON_AUTHORITATIVE_INFORMATION(203), // 说明由客户端提出的后续信息不是源自服务器。
	SC_NOT_ACCEPTABLE(406), // 说明由请求消息标示的资源只能生成根据在请求消息中被发送的接受消息头来说有内容特征的响应实体。
	SC_NOT_FOUND(404), // 表明被请求的资源无效。
	SC_NOT_IMPLEMENTED(501), // 表明HTTP服务器不支持执行请求所需的功能。
	SC_NOT_MODIFIED(304), // 表明一个有条件的GET操作发现资源有效且没有被更改过。
	SC_OK(200), // 表示请求正常完成。
	SC_PARTIAL_CONTENT(206), // 表示服务器已经执行完部分对资源的GET请求。
	SC_PAYMENT_REQUIRED(402), // 保留以留为将来使用。
	SC_PRECONDITION_FAILED(412), // 表明在服务器上进行测试时，在一个或更多的请求消息头部域中给定的前提条件估计不成立。
	SC_PROXY_AUTHENTICATION_REQUIRED(407), // 表示客户机必须首先用代理服务器对自己进行鉴别。
	SC_REQUEST_ENTITY_TOO_LARGE(413), // 表示服务器拒绝处理请求，因为请求实体比服务器愿意或能够处理的请求大。
	SC_REQUEST_TIMEOUT(408), // 表示在服务器可以等待的时间内没有产生一个请求。
	SC_REQUEST_URI_TOO_LONG(414), // 表示服务器拒绝处理请求，因为Request-URI比服务器愿意或能够处理的请求大。
	SC_REQUESTED_RANGE_NOT_SATISFIABLE(416), // 表示服务器不能为请求的字节范围提供服务。
	SC_RESET_CONTENT(205), // 说明代理应当重新设置导致请求被发出的文档视图。
	SC_SEE_OTHER(303), // 表明对于请求的响应消息能够在不同的URL中被发现。
	SC_SERVICE_UNAVAILABLE(503), // 表明HTTP服务器暂时超负荷，不能处理请求。
	SC_SWITCHING_PROTOCOLS(101), // 表示服务器正根据Upgrade header切换协议。
	SC_UNAUTHORIZED(401), // 表示请求需要进行HTTP认证。
	SC_UNSUPPORTED_MEDIA_TYPE(415), // 表示服务器拒绝服务于请求，因为请求实体是采用了不为被请求的方法的资源所支持。
	SC_USE_PROXY(305);// 表明被请求的资源必须通过 Location域告知代理服务器访问。

	// 响应码
	int code;

	ResponseCode(int code) {
		this.code = code;
	}
}
