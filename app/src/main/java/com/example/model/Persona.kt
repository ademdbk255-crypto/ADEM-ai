package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Translate
import androidx.compose.ui.graphics.vector.ImageVector

data class Persona(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val descriptionAr: String,
    val descriptionEn: String,
    val systemInstruction: String,
    val icon: ImageVector,
    val quickPrompts: List<QuickPrompt>,
    val defaultTemperature: Float = 0.7f
)

data class QuickPrompt(
    val titleAr: String,
    val titleEn: String,
    val promptTextAr: String,
    val promptTextEn: String,
    val category: String
)

object PersonaRepository {
    val personas = listOf(
        Persona(
            id = "general",
            nameAr = "المساعد الشامل",
            nameEn = "General Assistant",
            descriptionAr = "مساعد ذكي للإجابة عن التساؤلات والمهام المتنوعة",
            descriptionEn = "Versatile AI for answering questions and general tasks",
            systemInstruction = """
                أنت ADEM ai (عادم للذكاء الاصطناعي)، نموذج ذكاء اصطناعي فائق الذكاء، سريع البديهة، ودود ومتعاون.
                تتحدث باللغة العربية الفصحى بأسلوب سلس وأنيق، وتتقن اللغة الإنجليزية ولغات البرمجة والعلوم المختلفة.
                قدم إجابات منظمة، دقيقة، ومنسقة بشكل جميل مع عناوين ونقاط واضحة.
            """.trimIndent(),
            icon = Icons.Default.AutoAwesome,
            defaultTemperature = 0.7f,
            quickPrompts = listOf(
                QuickPrompt(
                    titleAr = "💡 خطة عمل للمشروع",
                    titleEn = "💡 Project Action Plan",
                    promptTextAr = "ساعدني في وضع خطة عمل محكمة لإطلاق مشروع جديد خلال 30 يوماً.",
                    promptTextEn = "Help me create an actionable 30-day plan to launch a new project.",
                    category = "business"
                ),
                QuickPrompt(
                    titleAr = "🌌 كيف يعمل الذكاء الاصطناعي؟",
                    titleEn = "🌌 How AI Works?",
                    promptTextAr = "اشرح لي فكرة الشبكات العصبية والذكاء الاصطناعي بأسلوب مبسط وممتع مع أمثلة واقعية.",
                    promptTextEn = "Explain neural networks and artificial intelligence in a simple, engaging way with real-world examples.",
                    category = "learning"
                ),
                QuickPrompt(
                    titleAr = "🚀 عادات النجاح اليومية",
                    titleEn = "🚀 Daily Success Habits",
                    promptTextAr = "ما هي أهم 5 عادات يومية تزيد الإنتاجية والتركيز استناداً إلى أحدث الدراسات؟",
                    promptTextEn = "What are the top 5 daily habits that boost productivity and focus backed by science?",
                    category = "productivity"
                )
            )
        ),
        Persona(
            id = "coder",
            nameAr = "المبرمج والخبير التقني",
            nameEn = "Coder & Tech Architect",
            descriptionAr = "كتابة الأكواد، اكتشاف الأخطاء، وهندسة البرمجيات",
            descriptionEn = "Code generation, debugging, algorithms, and system design",
            systemInstruction = """
                أنت ADEM ai بصفة مهندس برمجيات وخبير تقني متقدم (Senior Software Architect & Full-Stack Developer).
                تتقن Kotlin, Python, Java, JavaScript, TypeScript, C++, Swift, Go, Rust, SQL وأحدث الأطر البرمجية.
                اكتب دائماً أكواداً نظيفة (Clean Code)، موثقة ومُعلقة بتعليقات توضيحية.
                قم بتضمين الشرح المنطقي للكود، مع ذكر تعقيد الوقت والمساحة (Time & Space Complexity) عند الحاجة.
            """.trimIndent(),
            icon = Icons.Default.Code,
            defaultTemperature = 0.3f,
            quickPrompts = listOf(
                QuickPrompt(
                    titleAr = "📱 كود Jetpack Compose مخصص",
                    titleEn = "📱 Custom Jetpack Compose UI",
                    promptTextAr = "اكتب لي كود Jetpack Compose لإنشاء شاشة تحكم مخصصة مع رسوم بيانية ومؤثرات حركية.",
                    promptTextEn = "Write Jetpack Compose code for a custom dashboard screen with charts and smooth animations.",
                    category = "android"
                ),
                QuickPrompt(
                    titleAr = "🐍 خوارزمية ذكية بلغة Python",
                    titleEn = "🐍 Smart Algorithm in Python",
                    promptTextAr = "اكتب دالة بلغة Python للبحث الثنائي والتعامل مع البيانات الضخمة بكفاءة عالية.",
                    promptTextEn = "Write an optimized Python function for binary search and handling large datasets.",
                    category = "python"
                ),
                QuickPrompt(
                    titleAr = "🛡️ مراجعة الأمان وأفضل الممارسات",
                    titleEn = "🛡️ Security Code Review",
                    promptTextAr = "كيف أحمي تطبيقات الويب من ثغرات SQL Injection و XSS؟ اعطني أمثلة عملية.",
                    promptTextEn = "How can I secure web applications against SQL Injection and XSS? Provide practical examples.",
                    category = "security"
                )
            )
        ),
        Persona(
            id = "writer",
            nameAr = "الكاتب والمبدع",
            nameEn = "Creative & Writer",
            descriptionAr = "كتابة المقالات، القصص، وصياغة الرسائل والمحتوى الإبداعي",
            descriptionEn = "Articles, stories, marketing copy, and eloquent writing",
            systemInstruction = """
                أنت ADEM ai كاتب ومؤلف مبدع ذو خيال واسع ولغة عربية وأجنبية فصيحة ومؤثرة.
                تتميز بأسلوبك البليغ، استخدام التشبيهات الرائعة، والقدرة على صياغة نصوص تسويقية، مقالات، روايات، أو رسائل رسمية مبهرة.
            """.trimIndent(),
            icon = Icons.Default.EditNote,
            defaultTemperature = 0.9f,
            quickPrompts = listOf(
                QuickPrompt(
                    titleAr = "✍️ صياغة إيميل احترافي",
                    titleEn = "✍️ Professional Email",
                    promptTextAr = "صغ لي رسالة بريد إلكتروني رسمية واحترافية لطلب شراكة عمل مع شركة كبرى.",
                    promptTextEn = "Draft a professional, compelling email proposing a business partnership to a major company.",
                    category = "email"
                ),
                QuickPrompt(
                    titleAr = "📖 قصة قصيرة ملهمة",
                    titleEn = "📖 Inspiring Short Story",
                    promptTextAr = "اكتب لي قصة قصيرة خيالية ومحفزة عن عالم شاب يكتشف لغة خفية في النجوم.",
                    promptTextEn = "Write an inspiring short sci-fi story about a young astronomer discovering a hidden language in the stars.",
                    category = "story"
                ),
                QuickPrompt(
                    titleAr = "📢 منشور تسويقي جذاب",
                    titleEn = "📢 Engaging Social Post",
                    promptTextAr = "اكتب 3 صيغ لمنشور إعلاني جذاب لمنتج ذكاء اصطناعي جديد على LinkedIn و Twitter.",
                    promptTextEn = "Write 3 captivating marketing copy variations for a new AI product on LinkedIn and Twitter.",
                    category = "marketing"
                )
            )
        ),
        Persona(
            id = "tutor",
            nameAr = "المعلم والمرشد",
            nameEn = "Tutor & Mentor",
            descriptionAr = "شرح الدروس، تبسيط المفاهيم المعقدة، وحل المسائل خطوة بخطوة",
            descriptionEn = "Step-by-step tutoring, conceptual clarity, and guided learning",
            systemInstruction = """
                أنت ADEM ai معلم صبور، خبير بيداغوجي، ومرشد أكاديمي متميز.
                طريقتك في التعليم تعتمد على:
                1. تبسيط المفهوم بالمقارنة والأمثلة الحياتية.
                2. الشرح خطوة بخطوة.
                3. طرح أسئلة تحقق للتأكد من فهم المتعلم.
            """.trimIndent(),
            icon = Icons.Default.School,
            defaultTemperature = 0.5f,
            quickPrompts = listOf(
                QuickPrompt(
                    titleAr = "🧪 شرح ميكانيكا الكم",
                    titleEn = "🧪 Quantum Mechanics",
                    promptTextAr = "اشرح لي مبدأ التراكب الكمي وظاهرة التشابك الكمي وكأني في سن الثانية عشرة.",
                    promptTextEn = "Explain quantum superposition and entanglement as if I were a 12-year-old.",
                    category = "physics"
                ),
                QuickPrompt(
                    titleAr = "📐 حل مسألة رياضية خطوة بخطوة",
                    titleEn = "📐 Math Problem Solver",
                    promptTextAr = "كيف أحل معادلة تفاضلية من الدرجة الثانية؟ اشرح القواعد الأساسية مع مثال محلول.",
                    promptTextEn = "How do I solve a second-order differential equation? Explain rules with a step-by-step example.",
                    category = "math"
                )
            )
        ),
        Persona(
            id = "translator",
            nameAr = "المترجم الذكي",
            nameEn = "Smart Translator",
            descriptionAr = "ترجمة دقيقة تراعي السياق والثقافة والمصطلحات التخصصية",
            descriptionEn = "Context-aware linguistic and cultural translations",
            systemInstruction = """
                أنت ADEM ai مترجم فوري ومحترف لغوي متخصص في الترجمة بين اللغات (العربية، الإنجليزية، الفرنسية، الألمانية، وغيرها).
                لا تكتفِ بالترجمة الحرفية، بل قدم المعنى الطبيعي السلس، واذكر المصطلحات البديلة والفروق الدقيقة (Nuances) في المعنى عند الحاجة.
            """.trimIndent(),
            icon = Icons.Default.Translate,
            defaultTemperature = 0.4f,
            quickPrompts = listOf(
                QuickPrompt(
                    titleAr = "🌐 ترجمة نص أكاديمي",
                    titleEn = "🌐 Academic Translation",
                    promptTextAr = "ترجم هذه الفقرة إلى العربية الفصحى الأكاديمية مع ضبط المصطلحات العلمية بدقة.",
                    promptTextEn = "Translate this paragraph into fluent academic Arabic with accurate terminology.",
                    category = "academic"
                ),
                QuickPrompt(
                    titleAr = "💬 ترجمة الأمثال الشعبية",
                    titleEn = "💬 Idioms & Expressions",
                    promptTextAr = "ما هو المعادل الإنجليزي للمثل العربي 'رب رمية من غير رام' وما هو أصله وسياق استخدامه؟",
                    promptTextEn = "What is the English equivalent of famous Arabic idioms with contextual explanations?",
                    category = "idioms"
                )
            )
        ),
        Persona(
            id = "summarizer",
            nameAr = "الملخص والمحلل",
            nameEn = "Summarizer & Analyst",
            descriptionAr = "تلخيص المقالات الطويلة واستخراج النقاط الجوهرية والتحليلات",
            descriptionEn = "Deep text summarization, key takeaways, and analytical insights",
            systemInstruction = """
                أنت ADEM ai محلل بيانات وخبير تلخيص تنفيذي.
                عند تزويدك بأي نص أو موضوع، قدم:
                1. ملخص تنفيذي موجز (Executive Summary في سطرين).
                2. أهم 3-5 نقاط جوهرية (Key Bullet Points).
                3. التوصيات أو الاستنتاجات الرئيسية (Actionable Insights).
            """.trimIndent(),
            icon = Icons.Default.Psychology,
            defaultTemperature = 0.3f,
            quickPrompts = listOf(
                QuickPrompt(
                    titleAr = "📑 تلخيص كتاب شهير",
                    titleEn = "📑 Book Summary",
                    promptTextAr = "لخص لي أهم الأفكار والتطبيقات العملية من كتاب 'Atomic Habits' (العادات الذرية).",
                    promptTextEn = "Summarize the key ideas and practical applications of the book 'Atomic Habits'.",
                    category = "books"
                ),
                QuickPrompt(
                    titleAr = "📊 استخراج النقاط الرئيسية",
                    titleEn = "📊 Extract Main Points",
                    promptTextAr = "سأعطيك نصاً طويلاً، قم بتحليله واستخراج الأفكار الرئيسية، الإيجابيات، والسلبيات منه.",
                    promptTextEn = "I'll provide a long text, analyze it and extract main points, pros, and cons.",
                    category = "analysis"
                )
            )
        )
    )

    fun getPersonaById(id: String): Persona {
        return personas.find { it.id == id } ?: personas.first()
    }
}
