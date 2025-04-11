# 📦 Tugas Kecil 2 - Kompresi Gambar dengan Quadtree

## 🧠 A. Deskripsi Singkat Program
Program ini merupakan implementasi algoritma **Divide and Conquer** untuk melakukan **kompresi gambar** menggunakan struktur data **Quadtree**. Gambar berwarna akan dibagi menjadi blok-blok, dan dinilai berdasarkan tingkat homogenitas warna (RGB), dan representasi akhirnya adalah 
struktur pohon quadtree.

Program juga menyediakan fitur tambahan berupa **GIF visualisasi**, yang memperlihatkan proses kompresi bertahap dari gambar yang sangat kasar hingga mendekati bentuk kompresi dari input pengguna.

---

## ⚙️ B. Requirement & Instalasi

### Minimum Requirement:
- Java JDK 17 atau lebih tinggi
- Compiler terminal seperti `javac`

### Library Built-in:
- `javax.imageio.*` *(sudah tersedia di Java SE)*
- Tidak ada library eksternal tambahan yang perlu diunduh

### Struktur Folder:
```
├── src
│   ├── *.java   (semua file sumber Java)
├── bin          (hasil kompilasi .class)
├── output.png   (hasil kompresi)
├── output.gif   (hasil animasi kompresi)
```

---

## 🛠️ C. Cara Kompilasi Program

Jika di bin belum ada file .class (by default seharusnya sudah ada):
Jalankan perintah berikut dari direktori root proyek :

```bash
# Masuk ke folder src 
cd src

# Kompilasi semua file java ke folder bin
javac -d ../bin *.java 
```

---

## 🚀 D. Cara Menjalankan Program

### Menjalankan program untuk menghasilkan gambar hasil kompresi:
```bash
cd bin
java Main
```

### Menjalankan program untuk menghasilkan animasi GIF:
```bash
cd bin
java Main
```
> Pilih "y" ketika ditanya apakah ingin menghasilkan GIF visualisasi.

### Input Program:
- Path absolut gambar (format `.jpg`, `.png`), misal C:/Asus/Downloads/..
- Threshold (angka double, misalnya `10.0`)
- Ukuran minimum blok (berbasis luas area, misalnya `100`)
- Metode error (Variance, MAD, Max Difference, Entropy)
- Output path absolut PNG (default), jpg, dan/atau GIF 

---

## 👤 E. Author

```
Nama      : Peter Wongsoredjo
NIM       : 13523039
Prodi     : Teknik Informatika
Mata Kuliah : IF2211 Strategi Algoritma
```

