package cn.polarismesh.agent.adapter.dubbo2.interceptor;

import cn.polarismesh.agent.plugin.dubbo2.interceptor.DubboClusterInvokerInterceptor;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor;

/**
 * 服务发现拦截器1：用于覆盖AbstractClusterInvoker.directory属性，重写list方法
 */
public class DubboClusterInvokerAdapter implements AroundInterceptor {
    private static final DubboClusterInvokerInterceptor interceptor = new DubboClusterInvokerInterceptor();

    @Override
    public void before(Object target, Object[] args) {
        interceptor.before(target, args);
    }

    /**
     * 在AbstractClusterInvoker的构造器执行之后进行拦截，修改this.directory为自定义Directory对象
     * 拦截方法：public AbstractClusterInvoker(Directory<T> directory, URL url)
     *
     * @param target    拦截方法所属的对象
     * @param args      拦截方法的入参  args[0] : Directory对象  args[1] : URL对象
     * @param result    拦截方法的返回值  构造器的result为null
     * @param throwable 拦截方法抛出的异常，无异常则为null
     */
    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        interceptor.after(target, args, result, throwable);
    }
}
