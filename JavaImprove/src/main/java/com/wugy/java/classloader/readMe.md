####classLoader能够完成以下工作：
#####1、在定义路径下查找自定义的class文件，因为我们需要的class文件不一定在classpath下面；
#####2、对自己要加载的类做特殊处理，如保证通过网络传输的类的安全性，可以将类经过加密后再传输，在加载到JVM前需对类的字节码再解密，这个过程可以在自定义的classloader中实现；
#####3、可以定义类的实现机制，如果检查已经加载的class文件是否被修改，如果修改了，可以重新加载这个类，从而实现类的热部署。