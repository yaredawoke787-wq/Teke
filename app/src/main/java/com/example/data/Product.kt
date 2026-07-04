package com.example.data

import com.example.R

data class Product(
    val id: Int,
    val titleEn: String,
    val titleAm: String,
    val descEn: String,
    val descAm: String,
    val price: Double,
    val imageRes: Int,
    val category: String,
    val rating: Float,
    val reviewsCount: Int,
    val specificationsEn: List<String>,
    val specificationsAm: List<String>,
    val isTrending: Boolean = false,
    val imageUrl: String? = null
) {
    // Dynamic accessors based on current language
    val localizedTitle: String
        get() = if (LocalizationManager.currentLanguage == Language.English) titleEn else titleAm

    val localizedDesc: String
        get() = if (LocalizationManager.currentLanguage == Language.English) descEn else descAm

    val localizedSpecs: List<String>
        get() = if (LocalizationManager.currentLanguage == Language.English) specificationsEn else specificationsAm
}

object Category {
    val list = listOf(
        CategoryItem("All", "✨", "ሁሉንም"),
        CategoryItem("Birthday", "🎂", "ልደት"),
        CategoryItem("Wedding", "💍", "ሰርግ"),
        CategoryItem("Graduation", "🎓", "ምረቃ"),
        CategoryItem("Anniversary", "🎁", "አመት በዓል"),
        CategoryItem("Valentine's", "🌹", "ፍቅረኛሞች"),
        CategoryItem("Flowers", "🌼", "አበባ"),
        CategoryItem("Chocolate", "🍫", "ቸኮሌት"),
        CategoryItem("Gift Boxes", "📦", "ሳጥኖች"),
        CategoryItem("Corporate Gifts", "🏢", "ድርጅታዊ")
    )
}

data class CategoryItem(
    val id: String,
    val icon: String,
    val labelAm: String
) {
    val localizedLabel: String
        get() = if (LocalizationManager.currentLanguage == Language.English) "$icon $id" else "$icon $labelAm"
}

object ProductCatalog {
    val items = listOf(
        Product(
            id = 1,
            titleEn = "Royal Crimson Rose & Truffle Box",
            titleAm = "የንጉሣውያን ቀይ አበባ እና ቸኮሌት ሳጥን",
            descEn = "An exquisite, handcrafted circular box lined with premium black velvet, featuring 24 select long-stemmed crimson roses preserved to perfection, accompanied by a drawer containing 16 handcrafted Belgian chocolate truffles dusted with edible 24K gold foil.",
            descAm = "ከፍተኛ ጥራት ባለው ጥቁር ቬልቬት የተሠራ ክብ ሳጥን፣ 24 በጥንቃቄ የተመረጡ ረጅም ግንድ ያላቸው ቀይ አበቦች እና 16 በ24 ካራት የወርቅ ቅጠል የተለበጡ የቤልጂየም ቸኮሌቶች በውስጡ የያዘ የቅንጦት ስጦታ።",
            price = 6800.0,
            imageRes = R.drawable.img_product_rose_chocolate,
            category = "Valentine's",
            rating = 4.9f,
            reviewsCount = 42,
            specificationsEn = listOf("24 Fresh Preserved Roses", "16 Gold-Dusted Truffles", "Velvet Textured Circular Box", "Satin Ribbons with Embossed Gold Brand"),
            specificationsAm = listOf("24 ትኩስ የተጠበቁ ቀይ አበቦች", "16 በወርቅ የተለበጡ ቸኮሌቶች", "ከቬልቬት የተሰራ ክብ የካርቶን ሳጥን", "የወርቅ ማህተም ያለበት የሐር ሪበን"),
            isTrending = true,
            imageUrl = "https://images.unsplash.com/photo-1518199266791-5375a83190b7?q=80&w=1200&auto=format&fit=crop"
        ),
        Product(
            id = 2,
            titleEn = "Presidential Gold Chronograph Set",
            titleAm = "የፕሬዝዳንት ወርቅ ክሮኖግራፍ ሰዓት እና ብዕር",
            descEn = "A masterful luxury gift chest featuring an automatic mechanical chronograph watch in brushed gold and sapphire crystal. Paired with a matching premium black lacquer lacquer fountain pen with a 14K gold nib, and a full-grain Italian leather slim wallet.",
            descAm = "የሚያምር የሰዓት ስጦታ ሳጥን። አውቶማቲክ ሜካኒካል ክሮኖግራፍ ሰዓት ከወርቅ እና ሰንፔር ክሪስታል የተሰራ። ከጥቁር ላከር ፎንቴን ብዕር እና ከጣሊያን ሌዘር ከተሰራ ቀጭን የኪስ ቦርሳ ጋር የቀረበ።",
            price = 18500.0,
            imageRes = R.drawable.img_product_watch_pen,
            category = "Anniversary",
            rating = 5.0f,
            reviewsCount = 28,
            specificationsEn = listOf("Automatic Mechanical Chronograph", "14K Gold Nib Fountain Pen", "Full-grain Italian Calf Leather", "Satin-lined Oakwood Chest"),
            specificationsAm = listOf("አውቶማቲክ ሜካኒካል ክሮኖግራፍ ሰዓት", "የ14 ካራት የወርቅ ጫፍ ያለው ብዕር", "ከጣሊያን ሌዘር የተሰራ ቀጭን የኪስ ቦርሳ", "የእንጨት ማከማቻ ሳጥን ከሐር ጨርቅ ጋር"),
            isTrending = true,
            imageUrl = "https://images.unsplash.com/photo-1522312346375-d1a52e2b99b3?q=80&w=1200&auto=format&fit=crop"
        ),
        Product(
            id = 3,
            titleEn = "Majestic Orchid & Champagne Basket",
            titleAm = "የማጀስቲክ ኦርኪድ አበባ እና ሻምፓኝ ስብስብ",
            descEn = "Celebrate milestones with a dramatic presentation. Includes rare cascading white Phalaenopsis orchids, a premium non-alcoholic sparkling grape elixir in a gilded bottle, premium crystal flutes, and a selection of gold-wrapped gourmet pralines.",
            descAm = "ውድ ቀኖችን በልዩ ሁኔታ ያክብሩ። እምብዛም የማይገኙ ነጭ አበቦች፣ በወርቅ የተለበጠ ከቀለም ነፃ የሆነ የሻምፓኝ ጠርሙስ፣ የክሪስታል መጠጫዎች፣ እና በወርቅ ወረቀት የታሸጉ ጣፋጭ ፕራሊኖች።",
            price = 9500.0,
            imageRes = R.drawable.img_luxury_hero_banner,
            category = "Wedding",
            rating = 4.8f,
            reviewsCount = 19,
            specificationsEn = listOf("Phalaenopsis Orchid Plant", "Sparkling Elixir in Gilded Vessel", "Two Crystal Flutes", "Luxury Suede Lined Picnic Basket"),
            specificationsAm = listOf("ውብ የPhalaenopsis ኦርኪድ ተክል", "በወርቅ የተለበጠ ሻምፓኝ", "ሁለት የክሪስታል መጠጫዎች", "ከሱፍ የተሰራ የስጦታ ቅርጫት"),
            isTrending = false,
            imageUrl = "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?q=80&w=1200&auto=format&fit=crop"
        ),
        Product(
            id = 4,
            titleEn = "Signature Handcrafted Gift Chest",
            titleAm = "የተከ ሲግኔቸር የጋብቻ እና የልደት ሳጥን",
            descEn = "Our ultimate signature gift chest. An obsidian wooden case containing a personalized engraved crystal plaque, an organic lavender scented soy wax candle in a marble container, an premium metallic cardholder, and premium dark chocolates.",
            descAm = "ትልቁ እና የእኛ መለያ የሆነው የስጦታ ሳጥን። ስምዎ የተቀረጸበት ክሪስታል፣ ከእብነበረድ የተሰራ ጥሩ መዓዛ ያለው የሻማ ማቅለጫ፣ የብረት ካርድ መያዣ፣ እና ምርጥ ጥቁር ቸኮሌቶችን የያዘ ድንቅ የእንጨት ሳጥን።",
            price = 12500.0,
            imageRes = R.drawable.img_luxury_gift_logo,
            category = "Birthday",
            rating = 4.9f,
            reviewsCount = 53,
            specificationsEn = listOf("Obsidian Hardwood Case", "Engraved Keepsake Crystal", "Marble Container Soy Candle", "Premium Dark Truffle Selection"),
            specificationsAm = listOf("ጥቁር ጠንካራ የእንጨት ሳጥን", "ስም የሚቀረጽበት ልዩ ክሪስታል", "ከእብነበረድ የተሰራ መዓዛ ያለው ሻማ", "የተመረጡ የጥቁር ቸኮሌት ዓይነቶች"),
            isTrending = true,
            imageUrl = "https://images.unsplash.com/photo-1549465220-1a8b9238cd48?q=80&w=1200&auto=format&fit=crop"
        ),
        Product(
            id = 5,
            titleEn = "Exotic Floral Sensation Dome",
            titleAm = "ልዩ የጌጥ አበቦች በክሪስታል ድንኳን",
            descEn = "An enchanting arrangement of fresh-cut Ecuadorian luxury roses, baby breaths, and pastel hydrangeas sealed beautifully within a custom tempered glass cloche dome with rotating wooden base, lit by delicate fairy micro-LEDs.",
            descAm = "በመስታወት ድንኳን ውስጥ የተቀመጡ ውብ የአበቦች ውህደት። በአነስተኛ ኤልኢዲ መብራቶች ያጌጡ፣ የሚሽከረከር የእንጨት ታችኛው ክፍል ያለው ክላሲክ የመስታወት መኖሪያ።",
            price = 5400.0,
            imageRes = R.drawable.img_product_rose_chocolate,
            category = "Flowers",
            rating = 4.7f,
            reviewsCount = 15,
            specificationsEn = listOf("Tempered Glass Cloche Dome", "Preserved Ecuadorian Roses & Hydrangeas", "Micro LED Warm Fairy Lights", "Oak Wood Rotating Base"),
            specificationsAm = listOf("ውብ የመስታወት ክዳን (ድንኳን)", "የተጠበቁ የኢኳዶር አበቦች", "የኤልኢዲ የጌጥ መብራቶች", "ከእንጨት የተሰራ የሚሽከረከር ታች"),
            isTrending = false,
            imageUrl = "https://images.unsplash.com/photo-1526047932273-341f2a7631f9?q=80&w=1200&auto=format&fit=crop"
        ),
        Product(
            id = 6,
            titleEn = "Bespoke Royal Truffle Tray",
            titleAm = "ቤስፖክ የሮያል ቸኮሌት ትሪ",
            descEn = "A collection of 36 dark, milk, and white luxury chocolate truffles infusing premium spices, salted caramel, and fine coffee flavors. Displayed elegantly on an brushed brass serving tray with geometric mirror glass inserts.",
            descAm = "36 በጥራት የተዘጋጁ ጥቁር、ወተት、እና ነጭ ቸኮሌቶች። በውስጣቸው የተለያየ የቅመማቅመም、የካራሜል እና የቡና ጣዕም የያዙ፣ በመስታወት ትሪ ላይ በምርጥ ዲዛይን የተደረደሩ።",
            price = 4500.0,
            imageRes = R.drawable.img_product_watch_pen,
            category = "Chocolate",
            rating = 4.9f,
            reviewsCount = 31,
            specificationsEn = listOf("36 Gourmet Spiced Truffles", "Brushed Brass Serving Tray", "Mirror Glass Geometric Detail", "Golden Tongs Included"),
            specificationsAm = listOf("36 የተመረጡ ጣፋጭ ቸኮሌቶች", "ከነሐስ የተሠራ የሚያምር መደገፊያ ትሪ", "የመስታወት ጌጣጌጥ ዲዛይን", "ትንሽ የወርቅ መቆንጠጫ ማካተቻ"),
            isTrending = false,
            imageUrl = "https://images.unsplash.com/photo-1548907040-4d42b5211514?q=80&w=1200&auto=format&fit=crop"
        ),
        Product(
            id = 7,
            titleEn = "Premium Leather Journal & Quill Set",
            titleAm = "የቅንጦት ሌዘር ማስታወሻ ደብተር እና ብዕር",
            descEn = "Hand-burnished Italian leather journal featuring lined parchment pages, a polished brass feather quill, and three shades of rich metallic calligraphy ink. Encased in a walnut presentation drawer.",
            descAm = "በእጅ የተሰራ የጣሊያን ሌዘር ማስታወሻ ደብተር፣ የነሐስ ላባ ብዕር እና የብረት ቀለም በዎልት የእንጨት ሳጥን ውስጥ።",
            price = 4800.0,
            imageRes = R.drawable.img_product_watch_pen,
            category = "Corporate Gifts",
            rating = 4.8f,
            reviewsCount = 22,
            specificationsEn = listOf("Hand-Burnished Italian Leather", "Polished Brass Feather Quill", "3 Metallic Calligraphy Inks", "Walnut Presentation Case"),
            specificationsAm = listOf("በእጅ የተሰራ የጣሊያን ሌዘር", "የተወለወለ የነሐስ ላባ ብዕር", "3 የብረት ቀለም ዓይነቶች", "የዎልት እንጨት ማከማቻ ሳጥን"),
            isTrending = false,
            imageUrl = "https://images.unsplash.com/photo-1517842645767-c639042777db?q=80&w=1200&auto=format&fit=crop"
        ),
        Product(
            id = 8,
            titleEn = "Elegance Pearl Jewelry Case",
            titleAm = "የዕንቁ ጌጣጌጥ ሳጥን እና የአንገት ሐብል",
            descEn = "An exquisite double-tier leather case lined with anti-tarnish velvet, housing a breathtaking South Sea cultured pearl necklace with a 14K white gold clasp and matching pearl drop earrings.",
            descAm = "ባለ ሁለት ፎቅ የሌዘር ሳጥን ከቬልቬት ጋር፣ ውብ የደቡብ ባህር ዕንቁ የአንገት ሐብል ከ14 ካራት የወርቅ ማያያዣ እና የዕንቁ ጉትቻዎች ጋር።",
            price = 14200.0,
            imageRes = R.drawable.img_product_rose_chocolate,
            category = "Anniversary",
            rating = 4.9f,
            reviewsCount = 14,
            specificationsEn = listOf("South Sea Cultured Pearls", "14K White Gold Clasp", "Double-Tier Velvet Case", "Matching Pearl Earrings"),
            specificationsAm = listOf("የደቡብ ባህር ዕንቁዎች", "የ14 ካራት የወርቅ ማያያዣ", "ባለ ሁለት ፎቅ የቬልቬት ሳጥን", "ተዛማጅ የዕንቁ ጉትቻዎች"),
            isTrending = true,
            imageUrl = "https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?q=80&w=1200&auto=format&fit=crop"
        ),
        Product(
            id = 9,
            titleEn = "Royal Ethiopian Coffee Ritual Chest",
            titleAm = "የተከ የኢትዮጵያ ቡና ባህላዊ የስጦታ ሣጥን",
            descEn = "Celebrate Ethiopia's rich heritage with a luxurious, modern mahogany chest. Includes select single-origin specialty Geisha coffee beans, a gilded traditional clay Jebena pot, six hand-painted fine porcelain cups, and organic cardamom-infused honey.",
            descAm = "የኢትዮጵያን ቡና በቅንጦት ያቅርቡ። የማሆጋኒ ሳጥን፣ ምርጥ የጌሻ ቡና ፍሬዎች፣ በወርቅ ያጌጠ ጀበና፣ ስድስት በስዕል ያጌጡ ሲኒዎች፣ እና የቅመም ማር የያዘ።",
            price = 5900.0,
            imageRes = R.drawable.img_luxury_gift_logo,
            category = "Gift Boxes",
            rating = 5.0f,
            reviewsCount = 67,
            specificationsEn = listOf("Premium Geisha Coffee Beans", "Gilded Clay Jebena Pot", "6 Hand-Painted Porcelain Cups", "Cardamom Organic Honey"),
            specificationsAm = listOf("ምርጥ የጌሻ ቡና ፍሬዎች", "በወርቅ ያጌጠ ባህላዊ ጀበና", "6 በስዕል ያጌጡ የሲኒዎች ስብስብ", "የቅመም መዓዛ ያለው ተፈጥሯዊ ማር"),
            isTrending = true,
            imageUrl = "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?q=80&w=1200&auto=format&fit=crop"
        ),
        Product(
            id = 10,
            titleEn = "Luxe Gold Velvet Cupcake Tier",
            titleAm = "የወርቅ ቬልቬት ቸኮሌት ኬክ ማማ",
            descEn = "A decadent tower of 12 artisan gold-dusted chocolate cupcakes, filled with silk ganache and Madagascar vanilla bean cream, served on a beautiful 3-tier gold velvet and brushed brass display stand.",
            descAm = "12 የወርቅ ዱቄት የተረጨባቸው ቸኮሌት ኬኮች በማዳጋስካር ቫኒላ ክሬም የተሞሉ፣ ባለ 3 ደረጃ የወርቅ ቬልቬት እና የነሐስ ማሳያ መቆሚያ ላይ የቀረቡ።",
            price = 3500.0,
            imageRes = R.drawable.img_product_rose_chocolate,
            category = "Chocolate",
            rating = 4.6f,
            reviewsCount = 18,
            specificationsEn = listOf("12 Artisan Cupcakes", "Edible 24K Gold Dusting", "Madagascar Vanilla Cream", "3-Tier Velvet & Brass Stand"),
            specificationsAm = listOf("12 በእጅ የተሰሩ ኬኮች", "ሊበላ የሚችል የ24 ካራት ወርቅ ዱቄት", "የማዳጋስካር ቫኒላ ክሬም", "ባለ 3 ደረጃ የወርቅ ማሳያ መቆሚያ"),
            isTrending = false,
            imageUrl = "https://images.unsplash.com/photo-1486427944299-d1955d23e34d?q=80&w=1200&auto=format&fit=crop"
        ),
        Product(
            id = 11,
            titleEn = "Academic Excellence Keepsake Set",
            titleAm = "የምረቃ ክብር ስጦታ እና የነሐስ ኮምፓስ",
            descEn = "Honor the brilliant graduate with a gold-trimmed leather folder, an engraved heavy brass compass symbolizing navigation of future success, and a gold-engraved Parker executive ballpoint pen.",
            descAm = "ምሩቃንን በክብር ስጦታ ያነቃቁ። የወርቅ ጌጥ ያለው የሌዘር ፎልደር፣ የወደፊት ስኬትን የሚያሳይ የተቀረጸ የነሐስ ኮምፓስ እና የፓርከር ኤክስኪዩቲቭ ብዕር።",
            price = 6200.0,
            imageRes = R.drawable.img_product_watch_pen,
            category = "Graduation",
            rating = 4.9f,
            reviewsCount = 40,
            specificationsEn = listOf("Gold-Trimmed Leather Folder", "Engraved Solid Brass Compass", "Parker Executive Pen", "Velvet Graduation Gift Box"),
            specificationsAm = listOf("የወርቅ ጌጥ ያለው የሌዘር ፎልደር", "የተቀረጸ የነሐስ ኮምፓስ", "የፓርከር ኤክስኪዩቲቭ ብዕር", "የቬልቬት የምረቃ ስጦታ ሳጥን"),
            isTrending = false,
            imageUrl = "https://images.unsplash.com/photo-1523050854058-8df90110c9f1?q=80&w=1200&auto=format&fit=crop"
        ),
        Product(
            id = 12,
            titleEn = "Gilded Porcelain Tea Set",
            titleAm = "በወርቅ የተለበጠ የሸክላ ሻይ ስኒ ስብስብ",
            descEn = "A masterpiece of bone china porcelain, delicately painted with detailed imperial borders and lavish 24K gold gilding. Includes a stately teapot, creamer, sugar vessel, and four elegant tea cups.",
            descAm = "በጣም የሚያምር የሸክላ ሻይ ስኒ ስብስብ፣ በ24 ካራት ወርቅ ያጌጠ። የሻይ ማፍያ፣ የስኳር ማቅረቢያ፣ እና አራት የሻይ ስኒዎችን የያዘ።",
            price = 11500.0,
            imageRes = R.drawable.img_luxury_hero_banner,
            category = "Corporate Gifts",
            rating = 4.8f,
            reviewsCount = 25,
            specificationsEn = listOf("Fine Bone China Porcelain", "Lavish 24K Gold Gilding", "Teapot, Creamer & Sugar Pot", "Four Cups & Saucers"),
            specificationsAm = listOf("ጥራት ያለው የሸክላ ስራ", "የቅንጦት የ24 ካራት ወርቅ ማጌጫ", "የሻይ ማፍያ እና ስኳር ማቅረቢያ", "አራት ሲኒዎች እና ማሳረፊያ ሳህኖች"),
            isTrending = true,
            imageUrl = "https://images.unsplash.com/photo-1576092768241-dec231879fc3?q=80&w=1200&auto=format&fit=crop"
        )
    )
}
