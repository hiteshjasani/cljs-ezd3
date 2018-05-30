# ezd3

[![Clojars Project](https://img.shields.io/clojars/v/org.jasani/ezd3.svg)](https://clojars.org/org.jasani/ezd3)

A clojurescript library to provide an easy interface to D3.

D3 is opinionated on how it wants to work with the DOM.  It will
interrogate the DOM finding elements that match selectors and
add/remove/modify them in place.

Reagent (and React) are opinionated as well.  They want you to modify
data structures which they flush one way to the DOM.  They don't query
the DOM so much as write to it.

Classically it's been a challenge to get both of these tools to work
together and that's what ezd3 is intended to solve.

## Usage

This library lets you insert a D3 graph component into Reagent just
like any other component.  The only difference is that you must pass a
function that will render the D3 graph.

```clojure
(defn d3-render [svg] ...)

...
  [:div
    [:h1 "My Great Graph"]
    [ezd3/d3svg {:width 200 :height 175 :on-render d3-render
                 :style {:border "1px solid #ddd"}}]
```

In this snippet, your d3-render code would be called when the d3svg
component is added to the DOM.  This means you can code in D3 just
like all the Javascript examples.  No more fighting to get D3 and
React to play well together.  It just works.

See the `examples/` directory for actual code.

## License

Copyright © 2018 Hitesh Jasani

Distributed under the BSD-2 Clause "Simplified" License.
