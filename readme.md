# Space UI
A collection of styled cljs/cljc components and inputs.

These components are used in [Lightpad's](https://lightpad.ai) UI system.

![look and feel 1](https://github.com/spacegangster/space-ui/blob/master/resources/inputs.png?raw=true)


## Usage
Browse namespaces, require a namespace, use `face`

```clj
(ns app.views.form
  (:require [space-ui.inputs.text :as inputs.text]))

(defn form [user]
  [:form
    [inputs.text/face
     {:comp/label "Username"
      :comp/name "username"
      :comp/value (:username user)
      :comp/on-change--value update-atom}]])
```


## Whys
- I want my components to have options / parameters as maps.
- I also want these maps keys to be namespaced.
- I don't see this in re-com.
- I want Grid CSS system with classes and modifiers
Also I think CSS Grid is more expressive than Flexbox, but I haven't documented layout classes yet.


## Docs
General docs are yet to be done.
But I've tried to leave good docstrings for every component.

You can see it in action in Lightpad.ai UI.


## Support me
Hey I'm using it myself for my projects, then update these docs during the rare moments of pure bliss.

BTC 1EzwGRMQwZy8hooLJL2X2QfhTZfpk4xhPD
ETH 0x9B714c5e3FF1b4a52f937A831d61e88a0D4A95E0

Support me and I'll do the docs and demo, thanks.

You can also buy a subscription on Lightpad.ai to support me.


## License
MIT


## Artistic components license

Components listed below are under
Attribution 4.0 International (CC BY 4.0).

I ask for attribution if you use one of the artistic components below:
  - space_ui/glitch_logo.cljc
  - space_ui/perspective_oscillator.cljc
  - space_ui/isometric_preloader.cljc
  - space_ui/pulsating_frames.cljc
  - space_ui/pulsating_frames_2.cljc

[More info](https://creativecommons.org/licenses/by/4.0/)
