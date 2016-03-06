# JavaSealedUnions

## ACKNOWLEDGEMENTS

This library was heavily inspired by [RxEither](https://github.com/eleventigers/rxeither) and the wonderful [Domain Driven Design](http://fsharpforfunandprofit.com/ddd/) talk by [Scott Wlaschin](https://github.com/swlaschin).

## RATIONALE
I recommend watching the talk linked above first to see what solutions this library provides.

Java's type system is considered not very powerful although it contains most OOP niceties. One of the most known absences are tagged unions or sealed classes, [available in languages like Kotlin](https://kotlinlang.org/docs/reference/classes.html#sealed-classes).

Most current approach is having a base class or interface `IMyContract` and implementing several of `IChildContractPlusExtras` for public scopes. Another alternative is having a public `abstract` class that is inherited by a small set of package-only classes. The problem with the first approach is the possibility of breaking encapsulation and being able to implement the interface by a 3rd party outside the desired outcomes. The second approach hides the implementations for you, which requires the use of runtime tools like `instanceof` to handle. Both cases have one common problem: they only allow association of classes that are of the same type, or in the same hierarchy.

The intent of this library is to create a set of classses that can host one or several arbitrary complex types. Historically other libraries like Guava or RxJava have provided types to solve this issue:

* The base case is `Optional<T>`, which is the union of two types: `Some<T>` and `None`.

* The next case is `Either<A, B>` or `Result<T, Exception>`, which wraps the union of two arbitrary types `Left<L>` and `Right<R>`, or `Success<T>` and `Failure<Exception>`.

For a higher number of parameters no abstraction is usually provided, and it's when other languages change to explicit union types. As Java doesn't have unions on the language, we have to continue abstracting away with 3 types (Left<L>, Middle<M> and Right<R>), 4 types, 5 types...etc. 

I'm calling them `Union1<T>` for `Optional`, `Union2<L, R>` for `Either`/`Result`, `Union3<L, M, R>`...up to `UnionN`, which for this library would be `Union9<A,B,C,D,E,F,G,H,I>`


## API
Now that we understand what abstraction has to provide, we have to define a public API:

*What defines a union?*

It's a container that allows storage of 2-N different types. It's container and implementation-agnostic. To be properly composable it requires using interface composition rather than abstract inheritance.

*What belongs in the interface?*

It needs to be able to dereference the types to obtain a single, inequivocous, result. It should avoid extra operations, exceptions, and error states.

How is this done in other languages?

- Nested ifs:

```
if (union.isOne()) { 
    One element = union.getOne(); /* do something with one*/ 
} else if (union.isTwo()) { 
    Two element = union.getTwo(); /* do something with two*/ 
} else...
```

 ... and so and so until 9. Problem? The api can be dereferenced using `getXXX()` without calling any of the `isXXX()` methods, leading to exceptions and unexpected states. It adds one unneeded extra operation.

- Polymorphism: every element in the union has the set of public methods so they never have to be dereferenced. Except when the types on the union are not of the same interface! The point of unions is joining types that may not relate to each other.

- Pattern matching: not available in Java. But the intent of a pattern matcher is double: either continue to another piece of code, or return a single element. This ties directly to the next two options.

- Continuations: provide the union with one method per type in it that tells how the dereferencing operation has to continue. It branches execution synchronously into the continuations without having to check types externally. The type checks happen internally as each implementation of the union knows only how to dereference itself. It doesn't allow representating incorrect states, dereferencing unavailable types, or any other causes of Exceptions save for NPEs.

- Joining: provide a function per element in the union that maps them back into a single, common type, that the current execution flow knows how to use. The mapping is applied synchronously and the flow continues on the same method.


### Basic interface
For my library I have chosen continuations and joining as the default methods in the interface. Optionally you can also require `isXXX()` methods for checks.

**NOTE: you should never ever require or implement `getXXX()` as a way of returning a type inside the union. It defeats the purpose of the library. Direct dereference is error-prone, tends to be abused by programmers, and has been cited as a mistake when creating `Optional`-like libraries.**


```
public interface Union2<Left, Right> {

    continue(Action1<Left> continuationLeft, Action1<Right> continuationRight);

    <R> R join(Func1<Left, R> mapLeft, Func1<Right, R> mapRight);

    public interface Identifiable<Left, Right> extends Union2<Left, Right> {

        boolean isLeft();

        boolean isRight();

    }
}
```

### Creation
Part of creating a union is that the union itself is a new type and has to be represented too. For this case it's been included one Factory interface per UnionN that can be extended and required to create each one of the elements in the union:

```
public interface Factory<Left, Right> {

    Union2<Left, Right> left(Left left);

    Union2<Left, Right> right(Right right);

}
```

## USAGE

### Generic wrappers
This set of classes are provided to wrap any class regardless of its type. They come in flavours from `Union1` to `Union9`.

```
public class Either<L, R> implements Union2.Factory<L, R> {

        public <L, R> Either<L, R> left(L value) {
            return new Left<>(value);
        }

        public <L, R> Either<L, R> right(R value) {
            return new Right<>(value);
        }
}

public class Left<L, R> implements Union2<L, R> {

    final L value;

    public Left(L value){
        this.value = value;
    }

    public void continue(Action1<L> continuationLeft, Action1<R> continuationRight) {
        continuationLeft.call(value);
    }

    public <T> T join(Func1<L, T> mapLeft, Func1<R, T> mapRight) {
        return mapLeft.call(value);
    }
}

public class Right<L, R> implements Union2<L, R> {

    final R value;

    public Right(R value){
        this.value = value;
    }

    public void continue(Action1<L> continuationLeft, Action1<R> continuationRight) {
        continuationRight.call(value);
    }

    public <T> T join(Func1<L, T> mapLeft, Func1<R, T> mapRight) {
        return mapRight.call(value);
    }
}

// Example

Either<Command, Exception> serverResponse = getResponse();
serverResponse.continue(getCommandExecutor::execute(), getUi()::showError());
```

### Typed wrappers

In case you want your unions to be driven by your domain, there are two approaches:

#### Factory class plus generic wrapper
A parent class gives a more especific access to its methods.
```
public class Salute {

    private static final Factory FACTORY_INSTANCE = new Factory();

    private static class Factory implements Union2.Factory<Dog, Neighbour> {

        public Salute left(Dog dog) {
            return new Left<Dog, Neighbour>(dog);
        }

        public Salute right(Neighbour neighbour) {
            return new Right<Dog, Neighbour>(neighbour);
        }
    }

    public static Salute dog(String name, int paws) {
        return FACTORY_INSTANCE.left<Dog, Neighbour>(new Dog(name, paws));
    }

    public static Salute neighbour(String name, String favouriteFood, boolean isLiked) {
        return FACTORY_INSTANCE.right<Dog, Neighbour>(new Neighbour(name, favouriteFood, isLiked));
    }

    private final Union2<Dog, Neighbour> either;

    Salute(Union2<Dog, Neighbour> either){
        this.either = either;
    }

    public Union2<Dog, Neighbour> openDoor(){
        return either;
    }
}


// Example
String salute = getSalute().openDoor().join(dog -> "Who's a good dog?", neighbour-> neighbour.isLiked? "Good morning, " + neighbour.name + "!" : "*grunt*");
getSalute().openDoor().continue(dogSaluter::salute(), neighbourSaluter::salute());
```

#### Subtyping
This ties up to the inheritance approach, except it's sealed and explicit. It can be done by both abstract classes or interfaces.

**As a personal recomendation I would avoid any inherited methods, overloading, or overriding in any of the child classes. Watch the DDD talk in the Acknowledgements section to better understand the use of union types as plain data. The example below breaks this rule by adding a new method `valid()`.**
```
public abstract class PaymentType implements Union3<CardPayment, PayPalPayment, BankTransferPayment> {

    public abstract boolean valid();

    private static final Factory FACTORY_INSTANCE = new Factory();

    private static class Factory implements Union3.Factory<CardPayment, PayPalPayment, BankTransferPayment> {

        public CardPayment left(String cardNo, String ccv) {
            return new CardPayment(cardNo, ccv);
        }

        public PayPalPayment middle(String paypalNo, String password) {
            return new PayPalPayment(paypalNo, password);
        }

        public BankTransferPayment right(String accNo) {
            return new BankTransferPayment(accNo);
        }
    }

    public static PaymentType card(String cardNo, String ccv) {
        return FACTORY_INSTANCE.left(cardNo, ccv);
    }

    public static PaymentType paypal(String paypalNo, String password) {
        return FACTORY_INSTANCE.middle(paypalNo, password);
    }

    public static PaymentType bankTransfer(String accNo) {
        return FACTORY_INSTANCE.right<Dog, Cat>(new Cat(name, favouriteFood, isAdorable));
    }
}

class CardPayment extends PaymentType {

    private final String cardNo;

    private final String ccv;

    CardPayment(String cardNo, String ccv){
        this.cardNo = cardNo;
        this.ccv = ccv;
    }
    
    public boolean valid() {
        return /* some logic here */
    }

    public void continue(Action1<CardPayment> continuationLeft, Action1<PayPalPayment> continuationMiddle, Action1<BankTransferPayment> continuationRight) {
        continuationLeft.call(value);
    }

    public <T> T join(Func1<CardPayment, T> mapLeft, Func1<PayPalPayment, T> mapMiddle, Func1<BankTransferPayment, T> mapRight) {
        return mapLeft.call(value);
    }
}

class PayPalPayment extends PaymentType {

    private final String user;

    private final String pwd;

    CardPayment(String user, String pwd){
        this.user = user;
        this.pwd = pwd;
    }
    
    public boolean valid() {
        return /* some logic here */
    }

    public void continue(Action1<CardPayment> continuationLeft, Action1<PayPalPayment> continuationMiddle, Action1<BankTransferPayment> continuationRight) {
        continuationMiddle.call(value);
    }

    public <T> T join(Func1<CardPayment, T> mapLeft, Func1<PayPalPayment, T> mapMiddle, Func1<BankTransferPayment, T> mapRight) {
        return mapMiddle.call(value);
    }
}


class BankTransferPayment extends PaymentType {

    private final String accountNo;

    CardPayment(String accountNo){
        this.accountNo = accountNo;
    }
    
    public boolean valid() {
        return /* some logic here */
    }

    public void continue(Action1<CardPayment> continuationLeft, Action1<PayPalPayment> continuationMiddle, Action1<BankTransferPayment> continuationRight) {
        continuationRight.call(value);
    }

    public <T> T join(Func1<CardPayment, T> mapLeft, Func1<PayPalPayment, T> mapMiddle, Func1<BankTransferPayment, T> mapRight) {
        return mapRight.call(value);
    }
}

// Example

PaymentType payment = getUserPayment();
if (payment.valid()) {
    payment.continue(paymentService::byCard(), paymentService::withPayPal(), paymentService::viaBankTransfer())
}
```

# License

Copyright (c) pakoito 2016

GNU GENERAL PUBLIC LICENSE

Version 3, 29 June 2007

See LICENSE.md
