package cn.polarismesh.agent.adapter.dubbo2;

import cn.polarismesh.agent.adapter.dubbo2.interceptor.*;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentException;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentMethod;
import com.navercorp.pinpoint.bootstrap.instrument.Instrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformCallback;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformTemplate;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformTemplateAware;
import com.navercorp.pinpoint.bootstrap.plugin.ProfilerPlugin;
import com.navercorp.pinpoint.bootstrap.plugin.ProfilerPluginSetupContext;

import java.security.ProtectionDomain;

public class DubboPlugin implements ProfilerPlugin, TransformTemplateAware {

    private TransformTemplate transformTemplate;

    @Override
    public void setup(ProfilerPluginSetupContext context) {
        this.addTransformers();
    }

    private void addTransformers() {
        transformTemplate.transform("org.apache.dubbo.rpc.protocol.AbstractProtocol", AbstractProtocolTransform.class);
        transformTemplate.transform("org.apache.dubbo.rpc.protocol.AbstractProxyProtocol", ProtocolTransform.class);
        transformTemplate.transform("org.apache.dubbo.rpc.protocol.dubbo.DubboProtocol", ProtocolTransform.class);
        transformTemplate.transform("org.apache.dubbo.rpc.protocol.thrift.ThriftProtocol", ProtocolTransform.class);
        transformTemplate.transform("org.apache.dubbo.rpc.cluster.support.AbstractClusterInvoker", AbstractClusterInvokerTransform.class);
        transformTemplate.transform("org.apache.dubbo.common.extension.ExtensionLoader", ExtensionLoaderTransform.class);
    }

    public static class AbstractProtocolTransform implements TransformCallback {
        @Override
        public byte[] doInTransform(Instrumentor instrumentor, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
            final InstrumentClass target = instrumentor.getInstrumentClass(loader, className, classfileBuffer);
            InstrumentMethod invokeMethod = target.getDeclaredMethod("refer", "java.lang.Class", "org.apache.dubbo.common.URL");
            if (invokeMethod != null) {
                invokeMethod.addInterceptor(DubboInvokerAdapter.class);
            }
            return target.toBytecode();
        }
    }

    public static class ProtocolTransform implements TransformCallback {
        @Override
        public byte[] doInTransform(Instrumentor instrumentor, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
            final InstrumentClass target = instrumentor.getInstrumentClass(loader, className, classfileBuffer);
            InstrumentMethod invokeMethod = target.getDeclaredMethod("export", "org.apache.dubbo.rpc.Invoker");
            if (invokeMethod != null) {
                invokeMethod.addInterceptor(DubboProviderAdapter.class);
            }
            return target.toBytecode();
        }
    }

    public static class AbstractClusterInvokerTransform implements TransformCallback {
        @Override
        public byte[] doInTransform(Instrumentor instrumentor, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
            final InstrumentClass target = instrumentor.getInstrumentClass(loader, className, classfileBuffer);
            InstrumentMethod constructor = target.getConstructor("org.apache.dubbo.rpc.cluster.Directory", "org.apache.dubbo.common.URL");
            if (constructor != null) {
                constructor.addInterceptor(DubboClusterInvokerAdapter.class);
            }
            InstrumentMethod invokeMethod = target.getDeclaredMethod("invoke", "org.apache.dubbo.rpc.Invocation");
            if (invokeMethod != null) {
                invokeMethod.addInterceptor(DubboInvokeAdapter.class);
            }
            return target.toBytecode();
        }
    }

    public static class ExtensionLoaderTransform implements TransformCallback {
        @Override
        public byte[] doInTransform(Instrumentor instrumentor, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
            final InstrumentClass target = instrumentor.getInstrumentClass(loader, className, classfileBuffer);
            InstrumentMethod invokeMethod = target.getDeclaredMethod("getOrCreateHolder", "java.lang.String");
            if (invokeMethod != null) {
                invokeMethod.addInterceptor(DubboLoadBalanceAdapter.class);
            }
            return target.toBytecode();
        }
    }

    @Override
    public void setTransformTemplate(TransformTemplate transformTemplate) {
        this.transformTemplate = transformTemplate;
    }
}
