# Dezo Client Mod (Minecraft 1.21.11, Fabric)

Klientowy mod brandingowy: "Dezo Client" w tytule okna i menu, bez logo Minecraft/Mojang, przebudowane menu glowne.

- Minecraft **1.21.11**, Fabric Loader >= 0.18.1, **Java 21**
- Build lokalnie: `./gradlew build` -> `build/libs/dezoclientmod-*.jar`
- GitHub Actions: kazdy push/PR buduje mod, jar jest w zakladce *Actions -> Artifacts*
- Release: `git tag v1.1.0 && git push --tags` tworzy GitHub Release z jarem
