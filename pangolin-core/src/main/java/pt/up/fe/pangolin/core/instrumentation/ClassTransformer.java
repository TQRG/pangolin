package pt.up.fe.pangolin.core.instrumentation;

import java.io.*;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import pt.up.fe.pangolin.core.runtime.Agent;

public class ClassTransformer implements ClassFileTransformer {

	private List<Pass> instrumentationPasses;

	public ClassTransformer(List<Pass> instrumentationPasses) {
		this.instrumentationPasses = instrumentationPasses;
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

		if (loader == null)
			return null;

		CtClass c = null;
		ClassPool cp = null;

		try {

			cp = ClassPool.getDefault();

			c = cp.makeClass(new ByteArrayInputStream(classfileBuffer));

			for (Pass p : instrumentationPasses) {
				boolean finish = false;

				switch (p.transform(c)) {
				case CANCEL: {
					c.detach();
					return null;
				}
				case FINISH: {
					finish = true;
				}
				case CONTINUE:
				default: {
					break;
				}
				}

				if (finish) {
					break;
				}

			}
			c.writeFile(".");

			return c.toBytecode();
		}
		catch (Exception e) {
			// e.printStackTrace();
			try {
				PrintWriter pr = new PrintWriter(new FileOutputStream(new File("./log.txt"), true));
				pr.append(e.getMessage() + "\n");
				pr.close();
			} catch (FileNotFoundException fileNotFoundException) {
				// fileNotFoundException.printStackTrace();
			}

		}

		return null;
	}

}
