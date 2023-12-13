(ns app.ui
  (:require [uix.core :refer [use-state defui $]]))

(defui title-bar []
  (let [[count set-count] (use-state 0)]
    ($ :div.title-bar
       ($ :h1 "Hello")
       ($ :p count)
       ($ :button {:on-click
                   (fn []
                     #?(:cljs (js/console.log "Sent from client"))
                     (set-count (inc count)))}
          "Click Me"))))
