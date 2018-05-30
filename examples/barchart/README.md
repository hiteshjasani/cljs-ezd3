# barchart

Example barchart using ezd3's D3 svg component.  This is an example of
the interop, not an example of how to do D3 barcharts the best way.

## Run

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).

## Dependencies

This example was built using Java 9.  If you're using Java 8 or prior,
you will want to comment out the following line in `project.clj`.

```clojure
:jvm-opts ["--add-modules" "java.xml.bind"]

```

## License

Copyright © 2018 Hitesh Jasani
