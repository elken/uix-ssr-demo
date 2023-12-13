(ns client.client
  (:require [uix.core :refer [$]]
            [uix.dom :as dom.client]
            [app.ui :as ui]))

(defn main []
  (dom.client/hydrate-root
   (js/document.getElementById "root")
   ($ ui/title-bar)))
