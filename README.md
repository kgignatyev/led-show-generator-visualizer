Transfer show runner to Raspberry Pi
---

```bash
./copy-code-to-rpi.sh
```


Get the visualizer running
---

```bash
mvn package exec:java -Dexec.mainClass="com.kgignatyev.leds.visualizer.PlayShowKt"
```

Create a new show
---
change effects sequence in `GenerateShow.kt` and run
```bash
mvn package exec:java -Dexec.mainClass="com.kgignatyev.leds.GenerateShowKt"
```

then visualizer should reflect the changes and play the show

![visualizer](docs/visualizer.png)
