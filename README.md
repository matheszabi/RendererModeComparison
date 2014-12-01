RendererModeComparison
======================
I wanted to compare a few renderer mode for a game loot thread in Android.
Once I have implemnted the "classic" SDK mode with canvas. On most of my devices gave me around 60 fps.
I took out the drawing method body, so on that case is nothing to draw but must be the fastest method with highest fps. It didn't change a lot.
Then I made my code "nice" as how we learn in university and as how in many job interview is asking and the IT Architects would like to see it.
Unbelieable, but my fps became 30-31 - from 60.

No  Iam sharing the code, to see you too.
- Without inheritance 
Declaring an abstract class and having 2 implementation: 
- one is the same code for rendering as without inheritance and 
- other is the empty method of the draw.

This also reveals the canvas.drawBitmap(curBitmap, null, dst, null); it is very fast

![ScreenShot](https://cloud.githubusercontent.com/assets/506560/5248060/7d8d04b6-7980-11e4-8c2e-e710b14daf8c.png)

The rendering task it is simple:
Similates 3 sprite frame animations with 10 keystames.
Now they are 3 digits and the numbers are some .png files.

![ScreenShot](https://cloud.githubusercontent.com/assets/506560/5248061/7d8eb86a-7980-11e4-8fc2-76867646f52e.png)
