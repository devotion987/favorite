### 观察者模式
#### 1、认识观察者模式：
##### 我们看看报纸和杂志的订阅是怎么回事：
* 报纸的业务技术出版报纸；
* 向某家报社订阅报纸，只要他们有新报纸出版，就会给你送来。只要你是他们的订户，你就会一直收到新报纸；
* 当你不想再看报纸的时候，取消订阅，他们就不会再送新报纸；
* 只要报社还在运营，就会一直有人向他们订阅或取消报纸。

#### 2、定义观察者模式：
* 观察者模式定义了对象之间的一对多依赖，这样以来，当一个对象改变状态时，他的所有依赖着都会收到通知并会自动更新；
* 当两个对象之间松耦合，他们依然可以交互，但是不太清楚彼此的细节，观察者模式提供了一种对象设计，让主题和观察者之间松耦合。

#### 3、实例：
1. 设计气象站：
2. 实现气象站:<br>
__自开发观察者设计模式__：<br>
__使用jdk内置的观察者模式__：java.util包内包含最基本的Observer接口和Observable类，这和我们的Subject接口和Observer接口很类似，先来看看Java内置的
    观察者模式是如何工作的： <br> 
> 1、如何把对象编程观察者？ <br>
    实现观察者java.util.Observer接口，然后调用任何Observable对象的addObserver()方法，不再当观察者时，调用deleteObserver（）方法；<br>
> 2、 观察者要如何送出通知？ <br>
    首先，需要利用扩展java.util.Observable接口产生“可观察者”类，然后需要两个步骤：<br>
    > 先调用setChanged()方法，标识状态已经改变的事实；<br>
    > 然后调用两种notifyObservers()方法中的一个:notifyObservers()或notifyObservers(Object arg) <br>
> 3、 观察者如何接收通知？<br>
    观察者实现了更新的方法：update(Observable o, Object arg)，如果你想“推”（push）数据给观察者，可以把数据当作数据对象传送给notifyObservers(arg)方法。否则，观察者必须从可观察者对象中“拉”（pull）数据。<br>
setChanged()方法的作用：该方法用来标记状态已经改变的事实，好让notifyObservers()知道当它被调用时应该更新观察者，可以查看源码：<br>
`setChanged() {`<br>
`changed=true;`<br>
`}`<br>
`notifyObservers(Object arg) {`<br>
`if(changed){`<br>
`for every observer in the list{`<br>
`call update(this,arg);}`<br>
`changed = false;`<br>
`}}`<br>
`notifyObservers() {`
	`notifyObservers(null);`
`}`

> setChanged()方法可以让你更适当的更新观察者，另外也有一个hasChanged()方法，告诉你changed标志的当前状态。<br>

__java.util.Observable的“黑暗面”__<br>
观察者是一个类而不是接口，这限制了他的使用和复用。<br>
首先，Observable是一个类，你必须设计一个类继承它，如果某类想同时具有Observable和另一个超类的行为，就会陷入两难，这限制了Observable的复用潜力；<br>
再则，查看Observable API，会发现setChanged()方法被保护起来了，这意味着：除非你继承Observable，否则无法创建Observable实例并组合到自己的对象中来。
这违反了设计模式的原则：__多用组合,少用继承__。
