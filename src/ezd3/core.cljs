(ns ezd3.core
  (:require [reagent.core :as r]
            [cljsjs/d3]))

(defn attrs
  "Helper fn to apply attribute map to element as attributes

  (attrs svg {\"width\" 400 \"height\" 200})

  is equivalent to

  (-> svg
      (.attr \"width\" 400)
      (.attr \"height\" 200))"
  [elem m]
  (doseq [[k v] m]
    (.attr elem k v))
  elem)

(defn styles
  "Helper fn to apply attribute map to element as styles

  (styles svg {\"border\"     \"1px solid black\"
               \"visibility\" \"hidden\"})

  is equivalent to

  (-> svg
      (.style \"border\"     \"1px solid black\")
      (.style \"visibility\" \"hidden\"))"
  [elem m]
  (doseq [[k v] m]
    (.style elem k v))
  elem)

(def default-props {:width 200 :height 200
                    :style {:border "1px solid black"}})

(defn d3svg
  "Reagent component for D3

  When this component mounts, it will invoke the provided `on-render`
  callback function with an `svg` and the client can do all the D3
  operations they need to render.

  (defn my-render-function [svg]
    (-> js/d3
        (.select svg)
        ...))

  [d3svg {:width 400 :height 200 :on-render my-render-function
          :style {:border \"1px solid #dddddd\"}}]
  "
  []
  (let [on-render-cb (atom nil)
        did-mount (fn [elem]
                    (let [render-cb @on-render-cb
                          svg (-> js/d3
                                  (.select (r/dom-node elem)))]
                      (when render-cb
                        (render-cb svg))
                      svg))
        on-render (fn []
                    (let [this (r/current-component)
                          props (r/props this)]
                      (reset! on-render-cb (:on-render props))
                      [:svg (dissoc props :on-render)]))]
    (r/create-class
     {:display-name        "d3svg"
      :reagent-render      on-render
      :component-did-mount did-mount})))
