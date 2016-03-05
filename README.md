## ACKNOWLEDGEMENTS

This library was heavily inspired by [RxEither](https://github.com/eleventigers/rxeither) and the wonderful [Domain Driven Design](fsharpforfunandprofit.com/ddd/) talk by [Scott Wlaschin](https://github.com/swlaschin).

## RATIONALE

Java doesn't have tagged unions or sealed classes.

Most common approach is doing IMyUnion and having a bunch of IAnotherGuyInTheUnion for public scopes, and AbstractMyUnion with GuyInteUntionImpl for private scopes. This makes it possible to break encapsulation and being able to implement the interface by a 3rd party outside the desired outcomes.

The intent here is to create a class that can host one of several predefined complex types. For 1 or 2 types, generic wrappers are user instead:

The base case is `Optional<T>`, which is the union of two types: `Some<T>` and `None`.

The next case is `Either<A, B>` or `Result<T, Exception>`, which wraps the union of two arbitrary types `Left<L>` and `Right<R>`, or `Success<T>` and `Failure<Exception>`.

Generally languages stop at this amount of parameters, then change to explicit union types.

Java doesn't have unions on the language, so we have to continue abstracting away with 3 types (Left<L>, Middle<M> and Right<R>), 4 types, 5 types...etc. I'm calling them `Union1<T>` for `Optional`, `Union2<L, R>` for `Either`, `Union3<L, M, R>`...up to `UnionN`, which for my library would be `Union9<A,B,C,D,E,F,G,H,I>`


## API

Now we have the abstraction, on to the API:

What defines a union? it's a container that allows storage of 2-N different types. It's container-agnostic, and it's implementation-agnostic. Interfaces it is then.

Basic interface: what does it need? it needs to be able to dereference the types to obtain its result. How is this done?

- Pattern matching: not in Java.

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

- Continuations: you provide the union with one method per type on it that tells how the operation has to continue. It branches execution immediatelly without having to check types externally. The type checks happen internally as each implementation of the union knows only how to dereference itself, but it doesn't allow for representing incorrect states.

- Joining: provide a function per element in the union that maps them back into a single, common type, that the current execution flow knows how to use.

For my library I have chosen continuations and joining as the default methods in the interface. Optionally you can also require `isXXX()` methods for checks, but never `getXXX()`.


```
public interface Union2<Left, Right> {

    continue(Action1<Left> continuationLeft, Action1<Right> continuationRight);

    <R> R join(Func1<Left, R> mapLeft, Func1<Right, R> mapRight);

    public interface IdentifiableUnion2<Left, Right> extends Union2<Left, Right> {

        boolean isLeft();

        boolean isRight();

    }
}
```

### Creation

Part of creating a union is that the union itself is a new type and has to be represented too. For this case I've included one Factory interface per UnionN that is required to create each one of the elements in the union:

```
public interface Union2Factory<Left, Right> {

    Union2<Left, Right> left(Left left);

    Union2<Left, Right> right(Right right);

}
```

## USAGE

### Generic wrappers

```
public class Either<L, R> implements Union2Factory<L, R> {

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

```
public class Salute {

    private static final Factory FACTORY_INSTANCE = new Factory();

    private static class Factory implements Union2Factory<Dog, Neighbour> {

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

### Typed wrappers and subtypes

```
public class PaymentType implements Union3<CardPayment, PayPalPayment, BankTransferPayment> {

    public abstract boolean valid();

    private static final Factory FACTORY_INSTANCE = new Factory();

    private static class Factory implements Union3Factory<CardPayment, PayPalPayment, BankTransferPayment> {

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
