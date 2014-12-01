RendererModeComparison
======================
I wanted to compare a few renderer mode for a game loop thread, in Android.
Once I have implemented the "classic" SDK mode with canvas. On most of my devices gave me around 60 fps.
I took out the drawing method body, so on that case is nothing to draw but must be the fastest method with highest fps. It didn't change a lot.
Then I made my code "nice" as how we learn in university and as how in many job interview is asking and the IT Architects would like to see it.
Unbelievable, but my fps became 30-31 - from 62. So make sure you use ?Iinterfaces and abstract classes everywhere, hehe, and let me just code "plain" and fast running stuff.

Now I am sharing the code, to see you too, -othervise you won't believe it.
- one implementation it is without inheritance ( as how it was at beginning )
Declaring an abstract class and having 2 implementation: 
- one is the same code for rendering as without inheritance and 
- other is the empty method of the draw.

This also reveals the ```canvas.drawBitmap (curBitmap, null, dst, null);``` it is very fast!

![ScreenShot](https://cloud.githubusercontent.com/assets/506560/5248060/7d8d04b6-7980-11e4-8c2e-e710b14daf8c.png)

The rendering task it is simple:
Simulates 3 sprite frame animations with 10 keystames.
Now they are 3 digits and the numbers are some .png files.

![ScreenShot](https://cloud.githubusercontent.com/assets/506560/5248061/7d8eb86a-7980-11e4-8fc2-76867646f52e.png)
