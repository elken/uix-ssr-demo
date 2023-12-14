(ns app.ui
  (:require [uix.core :refer [use-state defui $]]))

(defui button [{:keys [children] :as props}]
  ($ :button.bg-sky-500.text-slate-50.rounded-full.w-10.h-10
    (dissoc props :children)
    children))

(defui app []
  (let [[count set-count] (use-state 0)]
    ($ :.flex.flex-col.justify-center.items-center.h-screen.bg-gray-800
       ($ :div.shadow.flex.flex-col.items-center.p-4.bg-gray-700.rounded.text-white.border-l-4.border-b-4.border-gray-600
          ($ :img.animate-bounce
             {:src "https://raw.githubusercontent.com/pitch-io/uix/master/logo.png"
              :width 128})
          ($ :p "This counter should persist when you make changes to this, but sadly not when the page refreshes.")
          ($ :span.my-4.font-semibold
             "Go ahead and edit this file in "
             ($ :kbd.bg-gray-600.font-bold.rounded.px-1 "src/app/ui.cljc")
             " and watch the changes show up live!")
          ($ :.p-6
             ($ button {:on-click #(set-count dec)} "-")
             ($ :span.mx-4.text-xl {} count)
             ($ button {:on-click #(set-count inc)} "+"))))))
