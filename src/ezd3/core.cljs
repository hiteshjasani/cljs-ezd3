(ns ezd3.core
  (:require [reagent.core :as r]
            [cljsjs/d3]))

(defn attrs
  "Apply attribute map to element as attributes"
  [elem m]
  (doseq [[k v] m]
    (.attr elem k v))
  elem)

(defn styles
  "Apply attribute map to element as styles"
  [elem m]
  (doseq [[k v] m]
    (.style elem k v))
  elem)

(def default-attrs {"width" 200 "height" 200})
(def default-style {"border" "1px solid black"})

(defn d3svg
  ([did-mount-cb] (d3svg default-attrs default-style did-mount-cb))
  ([attr-map style-map did-mount-cb]
   (let [did-mount (fn [elem]
                     (let [svg (-> js/d3
                                   (.select (r/dom-node elem))
                                   (.append "svg")
                                   (attrs attr-map)
                                   (styles style-map))]
                       (did-mount-cb svg)))
         on-render (fn [width height did-mount-cb]
                     [:div])]
     (r/create-class
      {:display-name        "d3svg"
       :reagent-render      on-render
       :component-did-mount did-mount}))))
