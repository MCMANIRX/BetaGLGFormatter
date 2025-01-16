let's say you want to put DonkeyKongSidekickB over HammerBro. 

1. Drag and drop convert the DonkeyKongSidekickB.glg, rename it to HammerBro.glg, and replace the HammerBro.glg in the art/HammerBro folder.
2. Use GLTTool to reformat DonkeyKongSidekickB's beta formatted texture and rename and replace HammerBro's .glt. (NOTE 1/15/25: pyGLT works as well)
3. Replace HammerBro.sanim with a renamed copy of DonkeyKong.sanim.
4. In the art/animation folder, open HammerBro.shier (.shier files are a model's bones) and DonkeyKong.shier with a hex editor (for this tutorial I'll assume you use HxD).
5. Copy the 4-byte hash at 0x14 in the HammerBro file, navigate to 0x14 in the DonkeyKong file, and press CTRL+B to overwrite the hash in the DonkeyKong file.
6. Save this file as "HammerBro.shier"
7. Enjoy!


DIP#0942 6/1/21
thisispadding@gmail.com
 
