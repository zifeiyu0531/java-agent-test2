package cn.polarismesh.agent.adapter.dubbo2.interceptor;

import cn.polarismesh.agent.plugin.dubbo2.interceptor.DubboLoadBalanceInterceptor;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor;

/**
 * 服务发现拦截器2：用于将dubbo的LoadBalance替换为自己的LoadBalance
 */
public class DubboLoadBalanceAdapter implements AroundInterceptor {
    private static final DubboLoadBalanceInterceptor interceptor = new DubboLoadBalanceInterceptor();

    /**
     * 在ExtensionLoader的getOrCreateHolder()方法前进行拦截
     * 若本次调用的目的是获取LoadBalance对象，则替换为Polaris自己定义的LoadBalance
     * 拦截方法：private Holder<Object> getOrCreateHolder(String name)
     *
     * @param target 拦截方法所属的对象
     * @param args   拦截方法的入参  args[0] : 本次调用的name信息，如name="random"时表示要获取RandomLoadBalance对象
     */
    @Override
    public void before(Object target, Object[] args) {
        interceptor.before(target, args);
    }

    /**
     * 在LoadBalance对象获取结束之后进行拦截，打印日志查看LoadBalance对象是否正确
     */
    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        interceptor.before(target, args);
    }
}
