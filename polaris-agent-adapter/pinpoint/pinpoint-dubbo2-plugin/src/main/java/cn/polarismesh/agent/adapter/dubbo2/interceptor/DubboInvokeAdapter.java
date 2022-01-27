package cn.polarismesh.agent.adapter.dubbo2.interceptor;

import cn.polarismesh.agent.plugin.dubbo2.interceptor.DubboInvokeInterceptor;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor;

/**
 * 统计时延信息、上报服务调用结果
 */
public class DubboInvokeAdapter implements AroundInterceptor {
    private static final DubboInvokeInterceptor interceptor = new DubboInvokeInterceptor();

    @Override
    public void before(Object target, Object[] args) {
        interceptor.before(target, args);
    }

    /**
     * 在AbstractClusterInvoker的invoke()方法之后进行拦截，统计本次服务调用时延数据，上报polaris
     * public Result invoke(final Invocation invocation) throws RpcException
     *
     * @param target    拦截方法所属的对象
     * @param args      拦截方法的入参  args[0] : Invocation对象
     * @param result    拦截方法的返回值 Result对象
     * @param throwable 拦截方法抛出的异常，无异常则为null
     */
    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        interceptor.after(target, args, result, throwable);
    }
}
