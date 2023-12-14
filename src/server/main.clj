(ns server.main
  (:require
   [app.ui]
   [cider.nrepl :refer [cider-nrepl-handler]]
   [nrepl.server :as nrepl]
   [ring.middleware.resource :refer [wrap-resource]]
   [ring.middleware.reload :refer [wrap-reload]]
   [ring.adapter.jetty :as jetty]
   [hiccup2.core :as h]
   [hiccup.util :refer [raw-string]]
   [uix.core :refer [$]]
   [uix.dom.server :as dom.server])
  (:gen-class))

(defonce server (atom nil))

(defn index [inner]
  (str
     "<!DOCTYPE html>"
     (h/html
         [:html.no-js {:lang "en"}
          [:head
           [:meta {:charset "utf-8"}]
           [:meta {:http-equiv "x-ua-compatible" :content "ie=edge"}]
           [:meta {:name "description" :content ""}]
           [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
           [:title "SSR"]
           [:link {:rel "stylesheet" :href (str "/css/style.css?v=" (System/currentTimeMillis))}]]
          [:body
           [:div#root (raw-string inner)]
           [:script {:src "/js/main.js"}]]])))

(defn render-app [_]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body
   (-> ($ (var-get (resolve 'app.ui/app)))
       dom.server/render-to-static-markup
       index)})

(defn start-server []
  (reset! server
          (jetty/run-jetty
           (-> #'render-app
               (wrap-resource "public")
               wrap-reload)
           {:port 3000 :join? false}))
  (println "Server started on port 3000"))

(defn stop-server []
  (when @server
    (.stop @server)
    (reset! server nil)
    (println "Server stopped")))

(defn restart-server []
  (stop-server)
  (start-server))

(defn -main [& args]
  (start-server)
  (nrepl/start-server :port 1881 :handler cider-nrepl-handler)
  (println "nREPL server started on port 1881"))

(comment
  ;; View component HTML
  (dom.server/render-to-static-markup
   ($ (var-get (resolve 'app.ui/app))))

  ;; View page HTML
  (-> ($ (var-get (resolve 'app.ui/app)))
      dom.server/render-to-static-markup
      index)

  ;; Restart the server process
  (restart-server))
