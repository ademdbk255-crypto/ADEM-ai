package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.R
import com.example.ui.theme.AdemBorderSubtle
import com.example.ui.theme.AdemCyanPrimary
import com.example.ui.theme.AdemDarkSurface
import com.example.ui.theme.AdemDarkSurfaceVariant
import com.example.ui.theme.AdemIndigoSecondary
import com.example.ui.theme.AdemSuccessGreen
import com.example.ui.theme.AdemWarningAmber

@Composable
fun SettingsDialog(
    currentTemperature: Float,
    onSaveTemperature: (Float) -> Unit,
    showBackground: Boolean,
    onToggleBackground: (Boolean) -> Unit,
    currentBackgroundDim: Float,
    onSaveBackgroundDim: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    var tempValue by remember { mutableFloatStateOf(currentTemperature) }
    var bgEnabled by remember { mutableStateOf(showBackground) }
    var bgDimValue by remember { mutableFloatStateOf(currentBackgroundDim) }
    val isKeyConfigured = BuildConfig.GEMINI_API_KEY.isNotBlank() && BuildConfig.GEMINI_API_KEY != "MY_GEMINI_API_KEY"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.dp, AdemCyanPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.adem_ai_logo_1787206486323),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Text(
                    text = "إعدادات ADEM ai",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Background Image Option Card
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AdemDarkSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AdemBorderSubtle)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Wallpaper,
                                    contentDescription = null,
                                    tint = AdemCyanPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "خلفية الشاشة الشخصية",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = if (bgEnabled) "الخلفية مفعّلة" else "الخلفية معطلة",
                                        fontSize = 11.sp,
                                        color = if (bgEnabled) AdemCyanPrimary else Color(0xFF64748B)
                                    )
                                }
                            }
                            Switch(
                                checked = bgEnabled,
                                onCheckedChange = {
                                    bgEnabled = it
                                    onToggleBackground(it)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = AdemCyanPrimary,
                                    checkedTrackColor = AdemIndigoSecondary
                                ),
                                modifier = Modifier.testTag("background_switch")
                            )
                        }

                        if (bgEnabled) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "تعتيم الخلفية (وضوح النصوص)",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${(bgDimValue * 100).toInt()}%",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AdemCyanPrimary
                                )
                            }
                            Slider(
                                value = bgDimValue,
                                onValueChange = {
                                    bgDimValue = it
                                    onSaveBackgroundDim(it)
                                },
                                valueRange = 0.2f..0.95f,
                                colors = SliderDefaults.colors(
                                    thumbColor = AdemCyanPrimary,
                                    activeTrackColor = AdemCyanPrimary,
                                    inactiveTrackColor = Color(0xFF334155)
                                ),
                                modifier = Modifier.testTag("bg_dim_slider")
                            )
                        }
                    }
                }

                // API Key status card
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AdemDarkSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AdemBorderSubtle)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isKeyConfigured) Icons.Default.CheckCircle else Icons.Default.Key,
                            contentDescription = null,
                            tint = if (isKeyConfigured) AdemSuccessGreen else AdemWarningAmber,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "حالة اتصال API",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isKeyConfigured) "المفتاح متصل وجاهز للعمل" else "يتم استخدام المفتاح المخصص من لوحة Secrets",
                                fontSize = 11.sp,
                                color = if (isKeyConfigured) AdemSuccessGreen else Color(0xFFFBBF24)
                            )
                        }
                    }
                }

                HorizontalDivider(color = AdemBorderSubtle)

                // Temperature Slider (Creativity vs Precision)
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Thermostat,
                                contentDescription = null,
                                tint = AdemIndigoSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "درجة الإبداع (Temperature)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Text(
                            text = String.format("%.1f", tempValue),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = AdemCyanPrimary
                        )
                    }

                    Slider(
                        value = tempValue,
                        onValueChange = { tempValue = it },
                        valueRange = 0.0f..1.0f,
                        steps = 9,
                        colors = SliderDefaults.colors(
                            thumbColor = AdemCyanPrimary,
                            activeTrackColor = AdemCyanPrimary,
                            inactiveTrackColor = Color(0xFF334155)
                        ),
                        modifier = Modifier.testTag("temperature_slider")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("دقيق ومحدد (0.0)", fontSize = 10.sp, color = Color(0xFF64748B))
                        Text("إبداعي ومتنوع (1.0)", fontSize = 10.sp, color = Color(0xFF64748B))
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSaveTemperature(tempValue)
                    onToggleBackground(bgEnabled)
                    onSaveBackgroundDim(bgDimValue)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = AdemCyanPrimary)
            ) {
                Text("حفظ التغييرات", color = Color(0xFF090D16), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إغلاق")
            }
        }
    )
}
