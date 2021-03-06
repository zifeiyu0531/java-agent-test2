package cn.polarismesh.agent.adapter.dubbo2.interceptor;

import cn.polarismesh.agent.plugin.dubbo2.interceptor.DubboInvokerInterceptor;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor;

/**
 * 服务发现拦截器0：记录ip:port与Invoker对象的映射关系
 */
public class DubboInvokerAdapter implements AroundInterceptor {
    private static final DubboInvokerInterceptor interceptor = new DubboInvokerInterceptor();

    @Override
    public void before(Object target, Object[] args) {
        interceptor.before(target, args);
    }

    /**
     * 在AbstractProtocol生成Invoker对象之后进行拦截，将URL的host:port作为key，生成的Invoker对象作为value存入map结构
     * 拦截方法：public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException
     *
     * @param target    拦截方法所属的对象
     * @param args      拦截方法的入参  args[0] : Class<T>对象, args[1] : URL对象
     * @param result    拦截方法的返回值  Invoker对象
     * @param throwable 拦截方法抛出的异常，无异常则为null
     */
    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        interceptor.after(target, args, result, throwable);
    }
}
