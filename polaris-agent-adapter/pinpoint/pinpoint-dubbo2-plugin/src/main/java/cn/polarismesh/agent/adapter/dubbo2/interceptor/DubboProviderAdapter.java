package cn.polarismesh.agent.adapter.dubbo2.interceptor;

import cn.polarismesh.agent.plugin.dubbo2.interceptor.DubboProviderInterceptor;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor;

/**
 * 服务注册拦截器
 */
public class DubboProviderAdapter implements AroundInterceptor {
    private static final DubboProviderInterceptor interceptor = new DubboProviderInterceptor();

    @Override
    public void before(Object target, Object[] args) {
        interceptor.before(target, args);
    }

    /**
     * 在Dubbo注册至zookeeper之后向Polaris进行注册
     * 拦截方法：public <T> Exporter<T> export(final Invoker<T> invoker) throws RpcException
     *
     * @param target    拦截方法所属的对象
     * @param args      拦截方法的入参  args[0] : Invoker对象
     * @param result    拦截方法的返回值  Exporter对象
     * @param throwable 拦截方法抛出的异常，无异常则为null
     */
    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        interceptor.before(target, args);
    }
}