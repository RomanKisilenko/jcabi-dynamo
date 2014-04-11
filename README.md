<img src="http://img.jcabi.com/logo-square.png" width="64px" height="64px" />
 
[![Build Status](https://travis-ci.org/jcabi/jcabi-dynamo.svg?branch=master)](https://travis-ci.org/jcabi/jcabi-dynamo)

More details are here: [dynamo.jcabi.com](http://dynamo.jcabi.com/index.html)

Set of classes in `com.jcabi.dynamo`
is an object layer on top of
[AWS SDK for Dynamo DB](http://aws.amazon.com/sdkforjava/).
For example, to read an item from your Dynamo table:

```java
public class Main {
  public static void main(String[] args) {
    Credentials credentials = new Credentials.Simple("AWS key", "AWS secret");
    Region region = new Region.Simple(credentials);
    Table table = region.table("foo");
    Collection<Item> items = table.frame().where("id", Conditions.equalTo(123));
    for (Item item : items) {
      System.out.println(item.get("name").getS());
    }
  }
}
```

You need just this dependency:

```xml
<dependency>
  <groupId>com.jcabi</groupId>
  <artifactId>jcabi-dynamo</artifactId>
  <version>0.10</version>
</dependency>
```

## Questions?

If you have any questions about the framework, or something doesn't work as expected,
please [submit an issue here](https://github.com/jcabi/jcabi-dynamo/issues/new).
If you want to discuss, please use our [Google Group](https://groups.google.com/forum/#!forum/jcabi).

## How to contribute?

Fork the repository, make changes, submit a pull request.
We promise to review your changes same day and apply to
the `master` branch, if they look correct.

Please run Maven build before submitting a pull request:

```
$ mvn clean install -Pqulice
```
