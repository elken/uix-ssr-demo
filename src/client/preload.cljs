(ns client.preload
  (:require [uix.dev]))

;; initializes fast-refresh runtime
(uix.dev/init-fast-refresh!)

;; called by shadow-cljs after every reload
(defn ^:dev/after-load refresh []
  ;; performs components refresh
  (uix.dev/refresh!))
