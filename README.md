# SMA21-LAB
SMA 2021 - Laborator

Morariu Dorian-Sebastian

Observatie
https://developer.android.com/guide/components/broadcasts

Android 8.0

Beginning with Android 8.0 (API level 26), the system imposes additional restrictions on manifest-declared receivers.

If your app targets Android 8.0 or higher, you cannot use the manifest to declare a receiver for most implicit broadcasts (broadcasts that don't target your app specifically). You can still use a context-registered receiver when the user is actively using your app.
