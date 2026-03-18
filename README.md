<div align="center">
  <img src="https://i.hizliresim.com/xxx.png" alt="LTarla Logo" width="200" />
  <h1>🌾 LTarla</h1>
  <p><b>Gelişmiş, Şansa Dayalı, Özel Tarlalar ve Etkinlikler ile Dolu Tarım Eklentisi!</b></p>
</div>

---

## 📖 Hakkında (About)
LTarla, Minecraft sunucunuz için sıradan buğday tarımını tamamen başka bir boyuta taşıyan eşsiz bir Skyblock / Survival eklentisidir.

WorldEdit kullanır gibi `LTarla Asası` ile belirli bir alanı seçip kendi tarlanızı oluşturabilirsiniz. Oluşturduğunuz her tarla tamamen **kendisine has ödüllere**, **şans oranlarına** ve **yenilenme sürelerine** sahip olur! 

Oyuncularınız buğdayları kırdığında buğday anında havaya (Air) dönüşür ve üzerinde çok gerçekçi ve sihirli köylü (VILLAGER_HAPPY) parçacıkları uçuşmaya başlar. Süre dolduğunda ise tek seferde sihirli bir ses ve görkemli bir parçacık efektiyle bir anda **Tamamen Büyümüş (Age 7)** olarak geri çıkar!

---

## ✨ Özellikler (Features)

- **🖌️ Gelişmiş Alan Seçimi:** Kendi yapısıyla `//wand` mantığında çalışır. Belirlediğiniz tarlalar yalnızca buğday (Wheat) kırılmasını özel olaya dâhil eder, oyuncular başka blokları kırdığında hiçbir sorun olmaz.
- **🎁 Tarlaya Özel Bağımsız Ödül Sistemi (Custom Drops):**
  - Her tarlanın kendi `yml` dosyası vardır (örn: `zones/vip_tarla.yml`).
  - Düşecek ödüller genel değil, tarlaya özeldir. `%1` şansla oyuncuya para verebilir, `%0.5` şansla özel isimli (Tarla Coin) eşyalar düşürebilirsiniz.
  - Sınır yok! İster komut çalıştırın (`eco give %player% 500`), ister özel eşyalar düşürün.
- **✨ Gerçekçi Parcha (Particle) Efektleri:** Kırılan buğdayların boşluklarında büyümesini beklerken, tabandan çıkıp havaya yükselen gerçekçi yeşil köylü (Villager Happy) yıldızları çıkar. Tamamen büyüdüğünde efsanevi bir efektle bir anda ortaya çıkar.
- **🎉 Otomatik "Tarla Yükselişi" Etkinlikleri:** Sistem, rastgele saat dilimlerinde (veya belirttiğiniz aralıklarla) sunucu çapında "Tarla Etkinliği" başlatır. 
  - Ekrandan ve sohbetten büyük duyurular geçer.
  - Etkinlik esnasında ayarladığınız `etkinlik_carpan` aktif olur (Örn: Çarpan 2 ise %1 düşme ihtimali olan eşya %2 ihtimalle düşer).

---

## 💻 Komutlar ve Yetkiler (Commands & Permissions)

| Komut | Yetki (Permission) | Açıklama |
| --- | --- | --- |
| `/ltarla wand` | `ltarla.admin` | Envanterinize alan seçimi için "LTarla Asası" verir. Sol tık: 1. Nokta, Sağ tık: 2. Nokta |
| `/ltarla create <TarlaIsmi>` | `ltarla.admin` | Asa ile seçtiğiniz bölgeyi yeni bir tarla olarak kaydeder. |
| `/ltarla reload` | `ltarla.admin` | `config.yml` ve tüm tarla dosyalarındaki değişikleri oyuna yansıtır. |

---

## 🛠️ Kurulum (Installation)
1. Eklentinin `<versiyon>.jar` dosyasını indirin.
2. Sunucunuzdaki `plugins/` klasörüne yapıştırın.
3. Sunucuyu yeniden başlatın. (Veya Plugman benzeri eklentilerle yükleyin)
4. Artık kullanıma hazır! Oyun içerisinden `/ltarla wand` ve `/ltarla create test` yaparak ilk tarlanızı oluşturun, `plugins/LTarla/zones/test.yml` üzerinden tarlanızın ödüllerini düzenleyin.

---

## ⚙️ Config & Zone Dosya Örneği (Configuration Preview)

**`plugins/LTarla/config.yml`**
```yaml
etkinlik_aktif: true
etkinlik_carpan: 2.0
etkinlik_suresi: 600
etkinlik_basladi_mesaj: "&e&lTARLA ETKINLIGI BASLADI!|&aTum tarlalarda esya dusme sansi &c2 Katina &acikti!"
etkinlik_bitti_mesaj: "&c&lTARLA ETKINLIGI BITTI!|&eSanslar normale dondu."
prefix: "&8[&eLTarla&8] "
```

**`plugins/LTarla/zones/baslangic_tarlasi.yml`**
```yaml
world: "world"
minX: -10.0
# ...
respawn-time: 60 # Saniye cinsinden yenilenme suresi
drops:
  ornek_para:
    chance: 1.0 # %1 Ihtimalle
    is-command: true # Esya degil komut calisacak
    commands:
      - "eco give %player% 100"
      - "msg %player% &a100 TL kazandin!"
  ornek_esya:
    chance: 2.0 # %2 Ihtimalle
    is-command: false # Gercek envantere esya verecek
    item:
      material: GOLD_NUGGET
      name: "&6&lTarla Coin"
      lore:
        - "&7Ozel tarladan dusen"
        - "&7degerli bir para birimi."
```

<div align="center">
  <br>
  <i>Spigot / Paper API 1.19 ve üzeri sürümlerle tam uyumlu şekilde çalışmaktadır.</i>
</div>
