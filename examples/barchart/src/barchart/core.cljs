(ns barchart.core
  (:require [cljs.reader :as reader]
            [reagent.core :as reagent :refer [atom]]
            [cljsjs/d3]
            [ezd3.core :as ezd3]
            ))

(enable-console-print!)

(def data
  [{:city "Brookline" :rats 40}
   {:city "Boston" :rats 90}
   {:city "Cambridge" :rats 30}
   {:city "Somerville" :rats 60}])

(defn d3-render
  [svg]
  (js/console.log "d3-render")

  (let [width      (reader/read-string (.attr svg "width"))
        height     (reader/read-string (.attr svg "height"))
        array-len  (count data)
        max-value  (.max js/d3 (into-array data) (fn [d] (get d :rats)))
        x-axis-len 100
        y-axis-len 100
        y-scale    (-> js/d3
                       (.scaleLinear)
                       (.domain #js [0 max-value])
                       (.range #js [0 y-axis-len]))
        tooltip    (-> js/d3
                       (.select "body")
                       (.append "div")
                       (ezd3/styles {"position"         "absolute"
                                     "padding"          "2px 5px"
                                     "background-color" "white"
                                     "opacity"          "0.8"
                                     "font-family"      "'Open Sans', sans-serif"
                                     "font-size"        "12px"
                                     "z-index"          "10"
                                     "visibility"       "hidden"}))]
    (js/console.log "width" width)
    (js/console.log "height" height)
    (js/console.log "array-len" array-len)
    (js/console.log "max-value" max-value)

    ;; draw bars
    (-> svg
        (.selectAll "rect")
        (.data (into-array data))
        (.enter)
        (.append "rect")
        (ezd3/attrs {:x      (fn [d i] (+ (* i (/ x-axis-len array-len)) 30))
                     :y      (fn [d] (- height (y-scale (get d :rats)) 75))
                     :width  (dec (/ x-axis-len array-len))
                     :height (fn [d] (y-scale (get d :rats)))
                     :fill   "steelblue"})
        (.on "mouseover"
             (fn [d]
               (-> tooltip
                   (.style "visibility" "visible")
                   (.text (str (get d :city) ": " (get d :rats))))))
        (.on "mousemove"
             (fn [d]
               (let [y (.. js/d3 -event -pageY)
                     x (.. js/d3 -event -pageX)]
                 (-> tooltip
                     (.style "top" (str (- y 10) "px"))
                     (.style "left" (str (+ x 10) "px"))
                     (.text (str (get d :city) ": " (get d :rats)))))))
        (.on "mouseout"
             (fn [d]
               (.style tooltip "visibility" "hidden")))
        )

    ;; manually draw y-axis (not optimal, but builds character)
    (-> svg
        (.append "line")
        (ezd3/attrs {:x1           30 :y1 0 :x2 30 :y2 100
                     :stroke-width 2
                     :stroke       "black"}))

    ;; manually draw x-axis (not optimal, but builds character)
    (-> svg
        (.append "line")
        (ezd3/attrs {:x1           30 :y1 100 :x2 130 :y2 100
                     :stroke-width 2
                     :stroke       "black"}))

    ;; y-axis label
    (-> svg
        (.append "text")
        (ezd3/attrs {:class       "y label" :text-anchor "end"
                     :font-family "'Open Sans', sans-serif"
                     :font-size   "12px"
                     })
        (.text "No. of Rats")
        (.attr :transform (ezd3/transform 20 20 -90)))
    ))

(defn hello-world []
  [:div
   [:h1 "Barchart example"]
   [:p "The D3 graph is a component, similar to other Reagent components.  It can be styled, for example in this case with a faint grey border."]
   [ezd3/d3svg {:width 200 :height 175 :on-render d3-render
                :style {:border "1px solid #ddd"}}]
   [:p "See src/barchart/core.cljs for the source."]
   ])

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
