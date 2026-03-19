# LTarla

Advanced and customizable farming and event management plugin for Minecraft servers.

---

## Hakkında

LTarla, Minecraft sunucuları için geliştirilmiş, alan bazlı yapılandırılabilir bir tarım ve etkinlik yönetimi sistemidir. 

Sunucu yöneticileri, oyun içerisinde belirli bölgeleri "özel tarla" olarak tanımlayarak bu alanlara standart dışı düşme oranları (drop rates), özel ödüller ve yeniden doğma süreleri atayabilir. Skyblock ve Survival gibi oyun modlarında ekonomiyi desteklemek amacıyla tasarlanmıştır.

Kırılan ekinlerin yerine, yapılandırılan süre zarfında otomatik olarak yenisi büyür ve bu süreç optimize edilmiş parçacık (particle) efektleriyle desteklenir.

---

## Özellikler

- **Bölge Seçimi:** WorldEdit benzeri asal bir seçim aracıyla hızlı tarla bölgeleri oluşturabilme. Sistem sadece tanımlı bölgelerdeki ekinleri kontrol eder, dışarıdaki blok kırılımlarına müdahale etmez.
- **Bağımsız Ödül Sistemi (Custom Drops):**
  - Her bölge kendi özel yapılandırma (yml) dosyasına sahiptir.
  - Şansa bağlı olarak eşya düşürme veya doğrudan komut çalıştırma desteği (örneğin: `eco give %player% 500`).
- **Görsel Efektler:** Büyüme ve hasat işlemleri için performansı etkilemeyen, optimize edilmiş gerçekçi parçacık efektleri.
- **Tarla Etkinlikleri:** Sistem, zaman yönetimi aracılığıyla sunucu genelinde rastgele etkinlikler başlatabilir. Etkinlik süresince tüm özel düşme oranları, ayarlanan çarpan değeri kadar geçici olarak artar.

---

## Komutlar ve Yetkiler

| Komut | Yetki | Açıklama |
| --- | --- | --- |
| `/ltarla wand` | `ltarla.admin` | Alan seçimi için gerekli asayı verir (Sol tık: 1. Nokta, Sağ tık: 2. Nokta). |
| `/ltarla create <TarlaIsmi>` | `ltarla.admin` | Seçilen alanı yeni bir tarla bölgesi olarak kaydeder. |
| `/ltarla reload` | `ltarla.admin` | Yapılandırma dosyalarını ve tarla verilerini yeniden yükler. |

---

## Kurulum

1. Eklentinin güncel `.jar` dosyasını indirebilir veya derleyebilirsiniz.
2. Sunucunuzun `plugins/` dizinine taşıyın.
3. Sunucunuzu yeniden başlatın.
4. Eklenti ilk başlatıldığında varsayılan `config.yml` ve ilk bölge dosyasını `plugins/LTarla` dizini altında otomatik oluşturacaktır.

---

## Konfigürasyon Örneği

**config.yml**
```yaml
etkinlik_aktif: true
etkinlik_carpan: 2.0
etkinlik_suresi: 600
etkinlik_basladi_mesaj: "&e&lTARLA ETKİNLİĞİ BAŞLADI!|&aTüm tarlalarda eşya düşme şansı &c2 Katına &açıktı!"
etkinlik_bitti_mesaj: "&c&lTARLA ETKİNLİĞİ BİTTİ!|&eŞanslar normale döndü."
prefix: "&8[&eLTarla&8] "
```

**zones/baslangic.yml**
```yaml
world: "world"
minX: -10.0
# ...
respawn-time: 60
drops:
  ornek_para:
    chance: 1.0 # %1
    is-command: true
    commands:
      - "eco give %player% 100"
      - "msg %player% &a100 TL kazandın!"
  ornek_esya:
    chance: 2.0 # %2
    is-command: false
    item:
      material: GOLD_NUGGET
      name: "&6&lTarla Coin"
      lore:
        - "&7Özel tarladan düşen"
        - "&7değerli para birimi."
```

*Spigot / Paper API 1.19 ve üzeri sürümlerle uyumludur.*