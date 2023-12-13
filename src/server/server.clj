(ns server.server
  (:require
   [app.ui :as ui]
   [cider.nrepl :refer [cider-nrepl-handler]]
   [nrepl.server :as nrepl]
   [reitit.ring :as ring]
   [ring.adapter.jetty :as jetty]
   [hiccup2.core :as h]
   [hiccup.util :refer [raw-string]]
   [uix.core :refer [$]]
   [uix.dom.server :as dom.server])
  (:gen-class))

(defn index
  ([]
   (index ""))
  ([inner]
   (h/html
    [:html.no-js {:lang "en"}
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:http-equiv "x-ua-compatible"
              :content "ie=edge"}]
      [:meta {:name "description" :content ""}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
      [:title "Uix SSR Demo"]]
     [:body
      [:div#root
       (raw-string inner)]
      [:script {:src "/js/main.js"}]]])))

(defn home-page [_]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body
   (let [markup (dom.server/render-to-static-markup ($ ui/title-bar))
         page (str (index markup))]
     (str page))})

(def app
  (ring/ring-handler
    (ring/router
     [["/" {:get home-page}]])
    (ring/routes
     (ring/create-resource-handler {:path "/"})
     (ring/create-default-handler))))

(defonce server (atom nil))

(defn start-server []
  (reset! server
          (jetty/run-jetty #'app {:port 3000 :join? false}))
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
  (restart-server))
